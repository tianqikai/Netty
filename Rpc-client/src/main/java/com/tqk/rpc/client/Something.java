package com.tqk.rpc.client;

public class Something {
 public static void main(String[] args) {
     Other o = new Other();
     new Something().addOne(o);
     System.out.println("i:"+o.i);
     System.out.println(new Something().addOne(1));
 }
 public void addOne(final Other o) {
//        o = new Other();
     o.i++;
 }
 public int addOne(final int x) {
 //return ++x;
  return x + 1;
 }
}
class Other {
 public int i; 
}