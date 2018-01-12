import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ProjectFileWriter fileWriter = new ProjectFileWriter("H:\\wamp64\\www", "monProjet");

        ArrayList<MvcObject> objects = new ArrayList<>();
        ArrayList<Controller> controllers = new ArrayList<>();
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

        controllers.add(new Controller("Site", new ArrayList<>(){{add(new String[]{"index", "render", "empty"});}}));

        fileWriter.createProjecFolder(controllers);

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


        for (Controller controller: controllers){
            fileWriter.write(controller);
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
