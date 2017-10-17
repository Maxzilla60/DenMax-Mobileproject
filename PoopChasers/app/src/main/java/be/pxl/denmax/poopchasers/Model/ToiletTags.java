package be.pxl.denmax.poopchasers.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class for keeping a nice hashmap of all available tags
class ToiletTags {
    private HashMap<ToiletTag, Boolean> tags;

    // Construct by passing ToiletTags
    public ToiletTags(ToiletTag... ctags) {
        tags = new HashMap<ToiletTag, Boolean>();
        for (ToiletTag tagKey : ToiletTag.values()) {
            tags.put(tagKey, false);
        }
        for (ToiletTag ctagKey : ctags) {
            tags.put(ctagKey, true);
        }
    }

    // Adding a tag ("setting one to true")
    public void addTag(ToiletTag newTag) {
        tags.put(newTag, true);
    }

    // Removing a tag ("setting one to false")
    public void removeTag(ToiletTag removedTag) {
        tags.put(removedTag, false);
    }

    // Get all tags (that are set to true) as a nice list
    public List<ToiletTag> getTagsAsList() {
        List<ToiletTag> list = new ArrayList<ToiletTag>();
        for(Map.Entry<ToiletTag, Boolean> entry : tags.entrySet()) {
            if(entry.getValue()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    // Getter
    public HashMap<ToiletTag, Boolean> getTags() {
        return tags;
    }
}
