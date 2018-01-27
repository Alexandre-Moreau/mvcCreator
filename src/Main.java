import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ProjectFileWriter fileWriter = new ProjectFileWriter("H:\\wamp64\\www", "monProjet");

        ArrayList<MvcObject> objects = new ArrayList<>();
        ArrayList<MvcController> controllers = new ArrayList<>();
        fileWriter.setControllers(controllers); //Commenter pour retirer la liste du header
        ArrayList<String> fileNames = new ArrayList<>();
        fileNames.add("index");
        fileNames.add("db");
        fileNames.add("controller");
        fileNames.add("model");
        fileNames.add("route");
        fileNames.add("tools");
        ArrayList<String> fileNamesHtml = new ArrayList<>();
        fileNamesHtml.add("header");
        fileNamesHtml.add("footer");

        controllers.add(new MvcController("Site", new ArrayList<>(){{add(new String[]{"index", "render", "empty"});}}));

        // --

        MvcObject article = new MvcObject("Article");
        article.addAtribute(new String[]{"nom","string","25"});
        article.addAtribute(new String[]{"chemin","string","50"});
        article.addAtribute(new String[]{"type","int"});
        objects.add(article);

        MvcTopObject.createControllersFromObjects(controllers, objects);

        MvcObject terme = new MvcObject("Terme");
        terme.addAtribute(new String[]{"motCle","string"});
        objects.add(terme);

        MvcObject langue = new MvcObject("Langue");
        langue.addAtribute(new String[]{"nom","string"});
        objects.add(langue);

        MvcObject concept = new MvcObject("Concept");
        concept.addAtribute(new String[]{"nom","string"});
        concept.addAtribute(langue);
        objects.add(concept);

        MvcObject reference = new MvcObject("Reference");
        reference.addAtribute(new String[]{"nombreRef","int"});
        reference.addAtribute(article);
        reference.addAtribute(concept);
        objects.add(reference);

        // --

        fileWriter.createProjecFolder(controllers);

        for (MvcController mvcController : controllers){
            fileWriter.write(mvcController);
        }

        for (MvcObject object: objects){
            fileWriter.write(object);
        }

        fileWriter.writeSql(objects);

        for (String fileName: fileNames){
            fileWriter.writeFile(fileName);
        }

        for (String fileNameHtml: fileNamesHtml){
            fileWriter.writeFileHtml(fileNameHtml);
        }
    }
}
