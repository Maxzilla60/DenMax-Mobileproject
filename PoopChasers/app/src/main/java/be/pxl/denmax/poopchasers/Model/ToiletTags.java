package be.pxl.denmax.poopchasers.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ToiletTags {
    private HashMap<ToiletTag, Boolean> tags;

    public ToiletTags(ToiletTag... ctags) {
        tags = new HashMap<ToiletTag, Boolean>();
        for (ToiletTag tagKey : ToiletTag.values()) {
            tags.put(tagKey, false);
        }
        for (ToiletTag ctagKey : ctags) {
            tags.put(ctagKey, true);
        }
    }

    public void addTag(ToiletTag newTag) {
        tags.put(newTag, true);
    }

    public void removeTag(ToiletTag removedTag) {
        tags.put(removedTag, false);
    }

    public List<ToiletTag> getTagsAsList() {
        List<ToiletTag> list = new ArrayList<ToiletTag>();
        for(Map.Entry<ToiletTag, Boolean> entry : tags.entrySet()) {
            if(entry.getValue()) {
                list.add(entry.getKey());
            }
        }
        return list;
    }

    public HashMap<ToiletTag, Boolean> getTags() {
        return tags;
    }
}
