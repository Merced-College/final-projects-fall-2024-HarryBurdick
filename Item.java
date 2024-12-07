public class Item {
    private String name;
    private String description;
    private String inspectionDetail;
    private String type;
    private Runnable action;
    private Runnable specialAction;

    // Constructor
    public Item(String name, String description, String inspectionDetail, String type, Runnable action) {
        this.name = name;
        this.description = description;
        this.inspectionDetail = inspectionDetail;
        this.type = type;
        this.action = action;
        this.specialAction = specialAction;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getInspectionDetail() {
        return inspectionDetail;
    }

    public String getType() {
        return type;
    }
    
    public Runnable getSpecialAction() {
    	return specialAction;
    }

    // Use the item
    public void use() {
        if (action != null) {
            action.run();
        }
    }
}
