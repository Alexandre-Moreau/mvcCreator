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

        MvcObject client = new MvcObject("Client");
        client.addAtribute(new String[]{"nom","string"});
        client.addAtribute(new String[]{"prenom","string"});
        client.addAtribute(new String[]{"age","int"});
        objects.add(client);

        MvcObject produit = new MvcObject("Produit");
        produit.addAtribute(new String[]{"nom","string50"});
        produit.addAtribute(new String[]{"marque","string20"});
        produit.addAtribute(new String[]{"prix","int"});
        objects.add(produit);

        MvcTopObject.createControllersFromObjects(controllers, objects);

        // --

        fileWriter.createProjecFolder(controllers);

        for (MvcController mvcController : controllers){
            fileWriter.write(mvcController);
        }

        for (MvcObject object: objects){
            fileWriter.write(object);
            fileWriter.writeSql(objects);
        }

        for (String fileName: fileNames){
            fileWriter.writeFile(fileName);
        }

        for (String fileNameHtml: fileNamesHtml){
            fileWriter.writeFileHtml(fileNameHtml);
        }
    }
}
