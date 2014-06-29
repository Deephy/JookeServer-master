package org.dartmouth.cache;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Yaozhong Kang
 * @date May 23, 2014
 */

public class LRUCache implements Serializable {

	private static final long serialVersionUID = -5377838555777217387L;

	protected HashMap<Object, Node> map;
	protected DoubleLinkedList list;
	private int capacity;

	public LRUCache(int capacity) {
		this.capacity = capacity;
		map = new HashMap<Object, Node>();
		list = new DoubleLinkedList();
	}

	public Object delete(Object key) {
		Node obj = map.get(key);
		if (obj == null) {
			return null;
		} else {
			Object result = obj.getVal();
			map.remove(key);
			list.delete(obj);
			return result;
		}
	}

	public Object get(Object key) {
		Node obj = map.get(key);
		if (obj == null) {
			return null;
		} else {
			list.moveToHead(obj);
			return obj.getVal();
		}
	}

	public void set(Object key, Object value) {
		Node obj = map.get(key);
		if (obj == null) {
			if (list.length == capacity) {
				Object kk = null;
				for (Object i : map.keySet()) {
					if (map.get(i) == list.tail) {
						kk = i;
						break;
					}
				}
				map.remove(kk);
				list.delete(list.tail);
			}
			obj = list.insert(new Node(value));
			map.put(key, obj);
		} else {
			obj.setVal(value);
			list.moveToHead(obj);
		}
	}

	public class Node {
		private Object val;
		Node next;
		Node previous;

		Node(Object v) {
			setVal(v);
			next = null;
			previous = null;
		}

		public Object getVal() {
			return val;
		}

		public void setVal(Object val) {
			this.val = val;
		}
	}

	public class DoubleLinkedList {
		private Node head;
		private Node tail;
		private int length;

		public DoubleLinkedList() {
			length = 0;
		}

		Node insert(Node n) {
			length++;
			if (head == null) {
				head = n;
				tail = n;
				return n;
			} else {
				n.next = head;
				head.previous = n;
				head = n;
				head.previous = null;
				return n;
			}
		}

		Node delete(Node n) {
			if (n == null) {
				return null;
			}
			if (n == head) {
				head = n.next;
				if (head != null) {
					head.previous = null;
				}
			} else {
				n.previous.next = n.next;
				if (n.next != null) {
					n.next.previous = n.previous;
				}
				if (n == tail) {
					tail = n.previous;
				}
			}
			length--;
			return n;
		}

		private void moveToHead(Node a) {
			if (a == null) {
				return;
			}
			if (a == head) {
				return;
			} else {
				delete(a);
				insert(a);
			}
		}
	}
}