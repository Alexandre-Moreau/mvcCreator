import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ProjectFileWriter {
    String projecFolder;
    String dbName;
    String dbUser = "root";
    String dbPassword = "";
    ArrayList<MvcController> controllers = new ArrayList<>();

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
        createDir(this.projecFolder + "controllers\\");
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
        try{
            File file;
            if(objectClass == "MvcObject"){
                file = new File(projecFolder + "models\\" + name + ".php");
            }else if(objectClass == "MvcController"){
                file = new File(projecFolder + "controllers\\" + name + "Controller.php");
                MvcController crtl = (MvcController) mvcTopObject;
                for (String[] function : crtl.getFunctions()) {
                    if(function[1] == "render"){
                        if(crtl.getName() != "Site"){
                            writeSimpleView(function, crtl.getObject());
                        }else{
                            writeEmptyFile(projecFolder + "views\\" + "site\\", "index.php");
                        }
                    }
                }
            }else{
                throw new Exception("Bad mvcTopObject type");
            }
            if (file.exists()) {
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " write " + file.getName() + " : file overwritten");
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
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " writeFile " + file.getName() + " : file overwritten");
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
                    if(!this.controllers.isEmpty()){
                        rawContent = MvcFiles.header(this.controllers);
                    }else{
                        rawContent = MvcFiles.header();
                    }
                    content = rawContent.split("\n");
                    break;
                default:
                    throw new Exception("Bad parameter");
            }
            if (file.exists()) {
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " writeFileHtml " + file.getName() + " : file overwritten");
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
        }catch(Exception e) {
            System.out.println(Colors.COLOR_ERROR + "error" + Colors.COLOR_RESET + " writeFileHtml \"" + fileName + "\" : " + e.getMessage());
        }
    }

    public void writeSimpleView(String[]function, MvcObject object){
        try{
            File file = new File(projecFolder + "views\\" + object.getName().toLowerCase() + "\\" + function[2] + function[0].substring(0, 1).toUpperCase() + function[0].substring(1) + ".php");
            if (file.exists()) {
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " writeSimpleView " + file.getName() + " : file overwritten");
            }else{
                System.out.println("create file " + file.getName());
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            String rawContent;
            String[] content;
            switch (function[2]){
                case "form":
                    rawContent = MvcFiles.formView(object);
                    content = rawContent.split("\n");
                    break;
                case "table":
                    rawContent = MvcFiles.tableView(object);
                    content = rawContent.split("\n");
                    break;
                case "empty":
                    rawContent = "";
                    content = rawContent.split("\n");
                    break;
                case "none":
                    rawContent = "";
                    content = rawContent.split("\n");
                    break;
                default:
                    rawContent = "";
                    content = rawContent.split("\n");
            }
            bw.newLine();
            for (String line: content) {
                bw.write(line);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }catch(Exception e){
            System.out.println(Colors.COLOR_ERROR + "error" + Colors.COLOR_RESET + " writeSimpleView \"" + 	object.getName() + "\\" + function[0] + "\" : " + e.getMessage());
        }
    }

    public void writeSql(ArrayList<MvcObject> objects) {
        try {
            File file = new File(this.projecFolder + "databaseScripts\\" + "create.sql");
            if (file.exists()) {
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " writeSql " + file.getName() + " : file overwritten");
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
            System.out.println(Colors.COLOR_ERROR + "error" + Colors.COLOR_RESET + " " + this.projecFolder + "dbScripts\\" + "create.sql : " + e.getMessage());
        }
    }

    public void writeEmptyFile(String folder, String name){
        try{
            File file = new File(folder + "\\" + name);
            if (file.exists()) {
                System.out.println(Colors.COLOR_WARNING + "warning" + Colors.COLOR_RESET + " writeEmptyFile " + file.getName() + " : file overwritten");
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
            System.out.println(Colors.COLOR_ERROR + "error" + Colors.COLOR_RESET + " writeEmptyFile \"" + folder + "\\" + name + "\" : " + e.getMessage());
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

    public void setControllers(ArrayList<MvcController> controllers) {
        this.controllers = controllers;
    }


}
