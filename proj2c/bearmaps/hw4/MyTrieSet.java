
package bearmaps.hw4;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class MyTrieSet implements TrieSet61B {
    private Node root;

    public MyTrieSet(){
        root = new Node(' ', false);
    }

    @Override
    public void clear(){
        root = new Node(' ', false);
    }

    @Override
    public boolean contains(String key){
        Node curr = root;
        for (int i = 0; i < key.length(); i ++){
            char c = key.charAt(i);
            if (curr.map.get(c) == null){
                return false;
            } else {
                curr = curr.map.get(c);
            }
        }
        return true;
    }

    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }


    @Override
    public List<String> keysWithPrefix(String prefix) {
        List<String> l = new ArrayList<>();
        Node curr = root;
        for (int i = 0, n = prefix.length(); i < n; i++) {
            curr = curr.map.get(prefix.charAt(i));
        }
        l = helper(l, curr, prefix);
        return l;
    }

    private List<String> helper(List<String> l, Node n, String prefix){
        if (n.map.isEmpty()){
            return l;
        }
        Set s = n.map.keySet();
        for (Object c : s){
            Node nd = n.map.get(c);
            if (nd.isKey){
                l.add(prefix + c);
            }
            helper(l, nd, prefix + c);
        }
        return  l;
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }

    private class Node{
        private boolean isKey;
        private char car;
        private HashMap<Character, Node> map;

        Node(char c, boolean isKey){
            this.isKey = isKey;
            this.car = c;
            map = new HashMap<>();
        }
    }
}
