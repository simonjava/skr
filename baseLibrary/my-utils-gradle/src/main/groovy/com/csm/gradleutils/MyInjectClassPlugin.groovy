package com.csm.gradleutils

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.csm.gradleutils.config.InjectConfig
import com.csm.gradleutils.transform.ReClassTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyInjectClassPlugin implements Plugin<Project> {

    void apply(Project project) {
        System.out.println("========================");
        System.out.println("Hello MethodStatisticsPlugin!");
        System.out.println("========================");

        project.extensions.create("injectConfig", InjectConfig.class)

        project.task('readExtension') << {
            def injectConfig = project['injectConfig']
            println "injectConfig: " + injectConfig
        }

// 仅处理application合包
        if (project.plugins.hasPlugin(AppPlugin.class)) {
            /**
             * android{}、compileSdkVersion、defaultConfig {}* 这些属性里面的值就是通过 Extension 被Android的Gradle插件读取到的。
             */
            def android = project.extensions.getByType(AppExtension.class)
            ReClassTransform reClassTransform = new ReClassTransform(project)
            android.registerTransform(reClassTransform)
        }
    }
}
