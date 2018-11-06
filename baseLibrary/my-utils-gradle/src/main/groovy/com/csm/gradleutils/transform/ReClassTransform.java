package com.csm.gradleutils.transform;

import com.android.build.api.transform.Context;
import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.build.gradle.internal.scope.GlobalScope;
import com.csm.gradleutils.config.InjectConfig;
import com.csm.gradleutils.processor.IProcessor;
import com.csm.gradleutils.processor.InjectGifProcessor;
import com.csm.gradleutils.processor.TimeStatisticsProcessor;
import com.csm.gradleutils.utils.GroovyU;
import com.csm.gradleutils.utils.U;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javassist.ClassPool;
import javassist.NotFoundException;

public class ReClassTransform extends com.android.build.api.transform.Transform {
    org.gradle.api.Project mProject;
    GlobalScope mGlobalScope;
    // 需要处理的 jar 包
    HashSet<String> mIncludeJars = new HashSet<>();
    HashMap<String, String> mMap = new HashMap<>();

    ArrayList<IProcessor> mProcessorList = new ArrayList<>();


    public ReClassTransform(org.gradle.api.Project project) {
        System.out.println("new ReClassTransform()");
        mProject = project;
        mGlobalScope = GroovyU.getGlobalScope(project);
        InjectConfig injectConfig = (InjectConfig) mProject.getProperties().get("injectConfig");
        if (injectConfig.isInjectMethodStatictis()) {
            mProcessorList.add(new TimeStatisticsProcessor());
        }
        if (injectConfig.isInjectGIf()) {
            mProcessorList.add(new InjectGifProcessor());
        }
    }

    // 设置我们自定义的Transform对应的Task名称
    // 类似：TransformClassesWithPreDexForXXX
    @Override
    public String getName() {
        return "输出每个方法的时间消耗";
    }

    // 指定输入的类型，通过这里的设定，可以指定我们要处理的文件类型
    //这样确保其他类型的文件不会传入
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    // 指定Transform的作用范围
    @Override
    public Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }


    /**
     * 会对每个 flavor 都来一遍啊
     */
    @Override
    public void transform(Context context, Collection<TransformInput> inputs,
                          Collection<TransformInput> referencedInputs,
                          TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        if (mProcessorList.size() == 0) {
            U.print(100,"不需要 inject！！！");
            // 完全依赖自己的
            super.transform(context, inputs, referencedInputs, outputProvider, isIncremental);
            return;
        }
        U.print(100, ">>>>>ReClassTransform");

        /* 初始化 ClassPool */
        ClassPool pool = initClassPool(inputs);

        /***开始注入***/
        for (TransformInput input : inputs) {
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                String path = directoryInput.getFile().getAbsolutePath();
                injectClass(path, pool);
            }

            for (JarInput jarInput : input.getJarInputs()) {
                File jar = jarInput.getFile();
                if (mIncludeJars.contains(jar.getAbsolutePath())) {
                    String dirAfterUnzip = mMap.get(jar.getParent() + File.separatorChar + jar.getName()).replace(".jar", "");
                    injectClass(dirAfterUnzip, pool);
                }
            }
        }

        /* 重打包 */
        repackage();
        /* 拷贝 class 和 jar 包 */
        copyResult(inputs, outputProvider);
    }

    private ClassPool initClassPool(Collection<TransformInput> inputs) {
        ClassPool pool = new ClassPool(true);
        List<String> allClassPath = U.getClassPaths(mProject, mGlobalScope, inputs, mIncludeJars, mMap);
        for (String p : allClassPath) {
            try {
                pool.insertClassPath(p);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return pool;
    }

    /**
     * 将解压的 class 文件重新打包，然后删除 class 文件
     */
    void repackage() {
        U.print(100, ">>> Repackage...");
        for (String it : mIncludeJars) {
            File jar = new File(it);
            String JarAfterzip = mMap.get(jar.getParent() + File.separatorChar + jar.getName());
            String dirAfterUnzip = JarAfterzip.replace(".jar", "");
            // println ">>> 压缩目录 $dirAfterUnzip"
            if (!new File(dirAfterUnzip).exists()) {
                U.print(100, "Repackage jar not exist path:" + dirAfterUnzip);
                continue;
            }
            U.zipDir(dirAfterUnzip, JarAfterzip);

            // println ">>> 删除目录 $dirAfterUnzip"
            try {
                FileUtils.deleteDirectory(new File(dirAfterUnzip));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拷贝处理结果
     */
    void copyResult(Collection<TransformInput> inputs,
                    TransformOutputProvider outputProvider) {
        U.print(100, ">>> copyResult...");
        for (TransformInput input : inputs) {
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                copyDir(outputProvider, directoryInput);
            }

            for (JarInput jarInput : input.getJarInputs()) {
                copyJar(outputProvider, jarInput);
            }
        }

    }

    /**
     * 拷贝目录
     */
    void copyDir(TransformOutputProvider output, DirectoryInput input) {
        File dest = output.getContentLocation(input.getName(), input.getContentTypes(), input.getScopes(), Format.DIRECTORY);
        try {
            FileUtils.copyDirectory(input.getFile(), dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        println ">>> 拷贝目录 ${input.file.absolutePath} 到 ${dest.absolutePath}"
    }

    /**
     * 拷贝 Jar
     */
    void copyJar(TransformOutputProvider output, JarInput input) {
        File jar = input.getFile();
        String jarPath = mMap.get(jar.getAbsolutePath());
        if (jarPath != null) {
            jar = new File(jarPath);
        }
        if (!jar.exists()) {
            U.print(100, "copyJar jar not exist path:" + jar.getPath());
            return;
        }
        String destName = input.getName();
        String hexName = DigestUtils.md5Hex(jar.getAbsolutePath());
        if (destName.endsWith(".jar")) {
            destName = destName.substring(0, destName.length() - 4);
        }
        File dest = output.getContentLocation(destName + '_' + hexName, input.getContentTypes(), input.getScopes(), Format.JAR);
        try {
            FileUtils.copyFile(jar, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void injectClass(String dir, ClassPool classPool) {
        for (IProcessor processor : mProcessorList) {
            processor.process(dir, classPool);
        }
    }

}
