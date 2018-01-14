import java.util.ArrayList;

public class MvcTopObject {
    String name;
    private ArrayList<String[]> functions = new ArrayList<String[]>();

    public String getName() {
        return name;
    }

    public static void createControllersFromObjects(ArrayList<MvcController> controllers, ArrayList<MvcObject> objects){
        for (MvcObject obj: objects) {
            //if(controllers.contains(obj)){
            MvcController ctrl = new MvcController(obj.getName());
            ctrl.addFunction(new String[]{"create", "render", "form"});
            ctrl.addFunction(new String[]{"showAll", "render", "table"});
            ctrl.addFunction(new String[]{"showById", "render", "table"});
            ctrl.setObject(obj);
            controllers.add(ctrl);
            //}
        }
    }
}
