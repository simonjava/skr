#! /bin/bash
echo ANDROID_SDK=$ANDROID_SDK


if [[ $1 == "" ]]; then
    adb shell am broadcast -a com.zq.live.FLUSH_LOG
    sleep 2
	rm -rf logs/
	adb pull /sdcard/Android/data/com.zq.live/files/logs ./

	for file in ./logs/*
	do
	    if test -f $file
	    then
	        python decode_mars_nocrypt_log_file.py $file
	    fi
	done
	echo sublime ./logs/
	sublime ./logs/
else
	if test -f $1 
	then
	    # 使用R8解混淆
	    java -jar ./r8_retrace/lib/retrace.jar ./publish/mapping.txt $1
		sublime $1
	else
		for file in $1/*
		do
		    if test -f $file
		    then
		    	if [[ $file == *".xlog" ]]; then
		    		python decode_mars_nocrypt_log_file.py $file
		    	fi
		    fi
		done
		echo sublime $1
		sublime $1
	fi
fi

echo 解析xlog歌词 ./logcat.sh ~/Downloads/logs  或者 ./logcat.sh 拉取sdcard中的歌词
echo 请使用 UTF-8 的编码，打开日志文件查看