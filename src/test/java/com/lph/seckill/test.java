package com.lph.seckill;


import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.omg.PortableInterceptor.INACTIVE;
import sun.reflect.generics.tree.Tree;

import java.util.*;

import static java.lang.Math.*;

public class test {
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
     }
    int num = 0;
    public List<List<Integer>> combine(int n, int k) {

    }


//static class LRUCache {
//    private int capacity;
//    private HashMap<Integer,LinkedNode> lruMap;
//    private LinkedNode head;
//    private LinkedNode tail;
//
//    class LinkedNode{
//        int key;
//        int value;
//        LinkedNode next;
//        LinkedNode pre;
//        public LinkedNode(int key,int value){
//            this.key = key;
//            this.value = value;
//            this.next = null;
//            this.pre = null;
//        }
//    }
//
//    public LRUCache(int capacity) {
//        this.capacity = capacity;
//        lruMap = new HashMap<>();
//        head = new LinkedNode(-1,-1);
//        tail = new LinkedNode(-1,-1);
//        head.next = tail;
//        tail.pre = head;
//    }
//
//    public int get(int key) {
//        if (lruMap.get(key) == null){
//            return -1;
//        }
//        LinkedNode preNode = lruMap.get(key);
//        LinkedNode resNode = preNode.next;
//        preNode.next = resNode.next;
//        resNode.pre = preNode;
//        lruMap.put(resNode.next.key,preNode);
//        resNode.next = null;
//        resNode.pre = null;
//        putHead(resNode);
//        return resNode.value;
//    }
//
//    public void put(int key, int value) {
//        if(lruMap.get(key) != null){
//            lruMap.get(key).next.value =value;
//            return;
//        }
//        LinkedNode newNode = new LinkedNode(key,value);
//        putHead(newNode);
//        if(lruMap.size() > capacity){
//            removeTail();
//        }
//    }
//
//    public void removeTail(){
//        LinkedNode tailNode = tail.pre;
//        tailNode.pre.next = tail;
//        tail.pre = tailNode.pre;
//        tailNode.next = null;
//        tailNode.pre = null;
//        lruMap.remove(tailNode.key);
//    }
//
//
//    public void putHead(LinkedNode node){
//        node.next = head.next;
//        head.next.pre = node;
//
//        head.next = node;
//        node.pre = head;
//
//        lruMap.put(node.next.key,node);
//        lruMap.put(node.key,head);
//    }
//}

}


