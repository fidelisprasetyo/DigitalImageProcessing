import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class Node {
    Byte ch;
    Integer freq;
    Node left = null;
    Node right = null;

    Node(Byte ch, Integer freq) {
        this.ch = ch;
        this.freq = freq;
    }

    Node(Byte ch, Integer freq, Node left, Node right) {
        this.ch = ch;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
}

public class HuffmanCoding {
    public static void createHuffmanTree(byte[] data) {
        if (data == null || data.length == 0) {
            return;
        }

        Map<Byte, Integer> freq = new HashMap<>();
        for (byte b : data) {
            freq.put(b, freq.getOrDefault(b, 0) + 1);
        }

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));
        for (var entry : freq.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() != 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            int sum = left.freq + right.freq;
            pq.add(new Node(null, sum, left, right));
        }

        Node root = pq.peek();
        Map<Byte, String> huffmanCode = new HashMap<>();
        encodeData(root, "", huffmanCode);

        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(huffmanCode.get(b));
        }

        System.out.println("Huffman Codes of the bytes are: " + huffmanCode);
        System.out.println("The encoded string is: " + sb);
        System.out.print("The decoded string is: ");
        if (isLeaf(root)) {
            while (root.freq-- > 0) {
                System.out.print(root.ch + " ");
            }
        } else {
            int index = -1;
            while (index < sb.length() - 1) {
                index = decodeData(root, index, sb);
            }
        }
    }

    public static void encodeData(Node root, String str, Map<Byte, String> huffmanCode) {
        if (root == null) {
            return;
        }

        if (isLeaf(root)) {
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");
        }
        encodeData(root.left, str + '0', huffmanCode);
        encodeData(root.right, str + '1', huffmanCode);
    }

    public static int decodeData(Node root, int index, StringBuilder sb) {
        if (root == null) {
            return index;
        }

        if (isLeaf(root)) {
            System.out.print(root.ch + " ");
            return index;
        }

        index++;
        root = (sb.charAt(index) == '0') ? root.left : root.right;
        index = decodeData(root, index, sb);
        return index;
    }

    public static boolean isLeaf(Node root) {
        return root.left == null && root.right == null;
    }

    public static void main(String[] args) {
        byte[] data = "javatpoint".getBytes();
        createHuffmanTree(data);
    }
}
