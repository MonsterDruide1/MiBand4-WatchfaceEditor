# MiBand4-WatchfaceEditor
This tool is able to build a WatchFace for the MiBand4. It supports many elements, like Hours, Minutes and Seconds, but also Steps/Distance, Status (Bluetooth connected?/Locked), date and more.

## Preparing
You can download the latest release from the releases page. For execution, you need the Java JRE, available at [java.com](https://java.com/de/download/).
At the start, you will be asked for the "Tool path". This program is based on the [MiBandWFTool](https://amazfitwatchfaces.com/forum/viewtopic.php?f=29&t=720), so you need to download that too.
After extracting the tool, you need to select the folder of the tool in the Dialog. To avoid this message, put the folder of the tool next to the .jar-file and name it "MiBandWFTool_1.3.8_Palette", with a new release of the Tool rename it to this version.

## Execution
After the start of the program, a error message opens: *"The background image hasn't been found, please put it in your project folder as "0000.png"!"*. It automatically tries to load the project in a folder called "data" next to the .jar-File.
To start a new Project, go to "File" -> "New" and select the folder it should go to. 
