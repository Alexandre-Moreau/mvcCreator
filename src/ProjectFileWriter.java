import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ProjectFileWriter {
    String projecFolder;
    String dbName;
    String dbUser = "root";
    String dbPassword = "";

    public ProjectFileWriter(String projectName) {
        this.projecFolder = System.getProperty("user.home") + "\\" +"Desktop\\" + projectName + "\\";
        this.dbName = projectName;
    }

    public ProjectFileWriter(String location, String projectName) {
        this.projecFolder = location + "\\" + projectName + "\\";
        this.dbName = projectName;
    }

    public void createProjecFolder(ArrayList<MvcController> mvcControllers){
        createDir(this.projecFolder);
        createDir(this.projecFolder + "mvcControllers\\");
        createDir(this.projecFolder + "models\\");
        createDir(this.projecFolder + "views\\");
        createDir(this.projecFolder + "databaseScripts\\");
        createDir(this.projecFolder + "css\\");
        createDir(this.projecFolder + "js\\");
        for (MvcController mvcController : mvcControllers) {
            createDir(this.projecFolder + "views\\" + mvcController.getName().toLowerCase());
        }
    }

    public void write(MvcTopObject mvcTopObject){
        String objectClass = mvcTopObject.getClass().getSimpleName();
        String name = mvcTopObject.getName().substring(0, 1).toLowerCase() + mvcTopObject.getName().substring(1);
        String rawContent = mvcTopObject.toString();
        String[] content = rawContent.split("\n");
        String[] childFiles;
        try{
            File file;
            if(objectClass == "MvcObject"){
                file = new File(projecFolder + "models\\" + name + ".php");
            }else if(objectClass == "MvcController"){
                file = new File(projecFolder + "controllers\\" + name + "MvcController.php");
                MvcController crtl = (MvcController) mvcTopObject;
                for (String[] function : crtl.getFunctions()) {
                    if(function[1] == "render"){
                        writeEmptyFile(projecFolder + "views\\" + name.toLowerCase() + "\\", function[0] + ".php");
                    }
                }
            }else{
                throw new Exception("Bad mvcTopObject type");
            }
            if (file.exists()) {
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " create file " + name + " : file overwritten");
            }else{
                System.out.println("create file " + file.getName());
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("<?php");
            bw.newLine();
            bw.newLine();
            bw.write("//Auto-generated file");
            bw.newLine();
            for (String line: content) {
                bw.write(line);
                bw.newLine();
            }
            bw.newLine();
            bw.write("?>");
            bw.flush();
            bw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void writeFile(String fileName){
        try{
            File file;
            String rawContent;
            String[] content;
            switch (fileName) {
                case "controller":
                    file = new File(projecFolder + "controllers\\" + "controller.php");
                    rawContent = MvcFiles.controller();
                    content = rawContent.split("\n");
                    break;
                case "db":
                    file = new File(projecFolder +  "db.php");
                    rawContent = MvcFiles.db(this.dbName, this.dbUser, this.dbPassword);
                    content = rawContent.split("\n");
                    break;
                case "index":
                    file = new File(projecFolder + "index.php");
                    rawContent = MvcFiles.index();
                    content = rawContent.split("\n");
                    break;
                case "model":
                    file = new File(projecFolder + "models\\" + "model.php");
                    rawContent = MvcFiles.model();
                    content = rawContent.split("\n");
                    break;
                case "route":
                    file = new File(projecFolder + "controllers\\" + "route.php");
                    rawContent = MvcFiles.route();
                    content = rawContent.split("\n");
                    break;
                case "tools":
                    file = new File(projecFolder + "tools.php");
                    rawContent = MvcFiles.tools();
                    content = rawContent.split("\n");
                    break;
                default:
                    throw new Exception("Bad parameter");
            }
            if (file.exists()) {
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " create file " + file.getName() + " : file overwritten");
            }else{
                System.out.println("create file " + file.getName());
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("<?php");
            bw.newLine();
            bw.newLine();
            bw.write("//Auto-generated file");
            bw.newLine();
            for (String line: content) {
                bw.write(line);
                bw.newLine();
            }
            bw.newLine();
            bw.write("?>");
            bw.flush();
            bw.close();
        }catch(Exception e){
            System.out.println(Colors.COLOR_ERROR + "error" + Colors.COLOR_RESET + " create file \"" + fileName + "\" : " + e.getMessage());
        }
    }

    public void writeFileHtml(String fileName){
        try{
            File file;
            String rawContent;
            String[] content;
            switch (fileName) {
                case "footer":
                    file = new File(projecFolder + "views\\" +   "footer.php");
                    rawContent = MvcFiles.footer();
                    content = rawContent.split("\n");
                    break;
                case "header":
                    file = new File(projecFolder + "views\\" +   "header.php");
                    rawContent = MvcFiles.header();
                    content = rawContent.split("\n");
                    break;
                default:
                    throw new Exception("Bad parameter");
            }
            if (file.exists()) {
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " create file " + file.getName() + " : file overwritten");
            }else{
                System.out.println("create file " + file.getName());
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("<!--Auto-generated file-->");
            bw.newLine();
            for (String line: content) {
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }catch(Exception e){
            System.out.println(Colors.COLOR_ERROR + "error" + Colors.COLOR_RESET + " create file \"" + fileName + "\" : " + e.getMessage());
        }
    }

    public void writeEmptyFile(String folder, String name){
        try{
            File file = new File(folder + "\\" + name);
            if (file.exists()) {
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " create file " + file.getName() + " : file overwritten");
            }else{
                System.out.println("create file " + file.getName());
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            //bw.write("/* <!--Auto-generated file--> */");
            bw.write("/*" + name + "*/");
            bw.newLine();
            bw.flush();
            bw.close();
        }catch(Exception e){
            System.out.println(Colors.COLOR_ERROR + "error" + Colors.COLOR_RESET + " create file \"" + folder + "\\" + name + "\" : " + e.getMessage());
        }
    }

    public void writeSql(ArrayList<MvcObject> objects) {
        try {
            File file = new File(this.projecFolder + "databaseScripts\\" + "create.sql");
            if (file.exists()) {
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " create file " + file.getName() + " : file overwritten");
            } else {
                System.out.println("create file " + file.getName());
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("/* Auto-generated file */\n");
            for(int i = objects.size() - 1; i >= 0; i--){
                String name = objects.get(i).getName().substring(0, 1).toLowerCase() + objects.get(i).getName().substring(1);
                bw.write("DROP TABLE IF EXISTS " + name + ";");
                bw.newLine();
            }
            bw.newLine();
            for (MvcObject object : objects) {
                String rawContent = object.toStringSql();
                String[] content = rawContent.split("\n");
                for (String line : content) {
                    bw.write(line);
                    bw.newLine();
                }
            }
            bw.flush();
            bw.close();
        }catch(Exception e){
            System.out.println(Colors.COLOR_ERROR + "error" + Colors.COLOR_RESET + " " + this.projecFolder + "dbScripts\\" + "create.sql" + e.getMessage());
        }
    }

    private void createDir(String name){
        File projecDir = new File(name);
        boolean result = false;

        if (!projecDir.exists()) {
            try{
                projecDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                System.out.println(Colors.COLOR_ERROR + "error" + Colors.COLOR_RESET + " create directory " + name + " : " + se.getMessage());
            }
            if(result) {
                System.out.println("create directory " + name);
            }
        }else{
            System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " create directory " + name + " : folder exists");
        }
    }


}
