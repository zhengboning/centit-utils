package com.centit.support.algorithm;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.centit.support.common.TreeNode;


/**
 * 
 * 一些通用的算法，这些算法都和具体的业务数据结构解耦
 * 
 * @author codefan
 * @version $Rev$ <br>
 *          $Id$
 */
public class ListOpt {
	/**
	 * 
	 * 判断两个对象是否是父子关系，用于针对树形展示的数据结构进行排序
	 * 
	 * @author codefan
	 * @version $Rev$ <br>
	 *          $Id$
	 */
	public static interface ParentChild<T> {
		boolean parentAndChild(T p,T c);
	}
	
	/**
	 * 交换List中两个对象的位置
	 * @param list
	 * @param p1 位置1
	 * @param p2 位置2
	 */
	public static <T> void changeListItem(List<T> list, int p1,int p2) {
		if(p1==p2)
			return;
		Collections.swap(list, p1, p2);
		/*T tmp = list.get(p1);
		list.set(p1, list.get(p2));
		list.set(p2, tmp);	*/	
	}
	/**
	 * 移动List一个对象到新的位置
	 * @param list
	 * @param item 位置1
	 * @param pos 位置2
	 */
	public static <T> void moveListItem(List<T> list, int item,int pos) {
		if(item==pos)
			return;
		/*T tmp = list.remove(item);
		list.add(pos, tmp);	*/
		int ns = Math.min(item,pos);
		int ne = Math.max(item,pos);
		if(ne>=list.size())
			return;
		T tmp = list.get(ns);
		for(int i=ns;i<ne-1;i++)
			list.set(i,list.get(i+1));
		list.set(ne, tmp);
	}
	

	/**
	 * 将数组结构按照树形展示的形式进行排序，将所有孩子元素放到父元素的下面
	 * 深度优先的排序
	 * @param list
	 * @param c
	 */
	public static <T> void sortAsTree(List<T> list, ParentChild<? super T> c) {
		int n=list.size();
		if(n<2)
			return ;
		//sorted 已经排序好的数量
		int sortedInd = 0;
		int [] parentInds = new int [n];
		while(sortedInd < n-1 ){
			// 找到所有的根节点
			int parentInd = -1;
			for(int i= sortedInd;i<n;i++){
				boolean isParent = true;
				for(int j=sortedInd;j<n;j++){
					if(i != j && c.parentAndChild(list.get(j),list.get(i))){
						isParent = false;
						break;
					}
				}
				if(isParent){
					parentInd = i;
					break;
				}
			}
			if(parentInd == -1) 
				break;			
			
			moveListItem(list,parentInd,sortedInd);
			parentInds[0]=sortedInd;
			sortedInd ++;
			int pathDeep=1;
			while(pathDeep>0){
				int newInsert = 0;
				for(int i=sortedInd;i<n;i++){
					if(c.parentAndChild(list.get(parentInds[pathDeep-1]),list.get(i))){
						moveListItem(list,i,sortedInd);
						parentInds[pathDeep]=sortedInd;
						pathDeep ++;
						sortedInd ++;
						newInsert ++;						
					}
				}
				if(newInsert==0){
					pathDeep--;
				}
			}			
			// 查找根节点的所有子元素
			//sortedInd = sortAsTreePiece(list,c,sortedInd);
		}
	}
	/**
	 * 对排序号的树形数组结构 找到JQueryTree要的Indexes
	 * @param list
	 * @param c
	 * @return
	 */
    public static <T> List<Integer> makeJqueryTreeIndex(List<T> list, ParentChild<? super T> c) {
    	List<Integer> indexes = new ArrayList<Integer>();
        int n=list.size();
        for(int i=0;i<n;i++){
        	int ind = 0;
        	for(int j=0;j<i;j++){
        		if(c.parentAndChild(list.get(j),list.get(i))){
        			ind = j+1;
                    break;
                }
        	}
        	indexes.add(ind);
        }
        return indexes;
    }
    
    /**
     * 移除List中的所有null对象
     * @param list
     * @return
     */
    public static <T> List<T> removeNullItem(List<T> list) {
   	
    	if(list==null||list.size()<1)
    		return null;
    	List<T> retList = new ArrayList<T>();
        for(T t:list){
        	if(t!=null)
        		retList.add(t);
        }

        return retList;
    }
    
    /**
     * 将TreeList转换为JSONArray
     * @param treeList  必须是已经通过 sortAsTree 排序好的list 
     * @param c  比较算法，需要实现接口 ListOpt.ParentChild<T>
     * @param childrenPropertyName 为孩子的 属性名
     * @return
     */
    public static <T> JSONArray treeToJSONArray
    		(List<T> treeList,ParentChild<? super T> c, String childrenPropertyName){
    	JSONArray jsonTree = new JSONArray();
    	Stack<T> treePath = new Stack<T>();
    	Stack<JSONObject> jsonPath = new Stack<JSONObject>();
    	int pathSum = 0;
    	for(T treeNode : treeList){    		
    		JSONObject jsonNode ;    		
    		if(ReflectionOpt.isScalarType(treeNode.getClass())){
    			jsonNode = new JSONObject();
    			jsonNode.put("value", StringBaseOpt.objectToString(treeNode));
    		}else
    			jsonNode = (JSONObject) JSON.toJSON(treeNode);
    		
    		while(true){	    		
	    		if(pathSum == 0 || 
	    				( pathSum>0 && c.parentAndChild(treePath.peek(),treeNode ))){
	    			if(pathSum == 0 ){
	    				jsonTree.add(jsonNode);
	    			}else{
	    				JSONObject parentJson = jsonPath.peek();
	    				JSONArray children =(JSONArray) parentJson.get(childrenPropertyName);
	    				if(children==null)
	    					children = new JSONArray();
	    				children.add(jsonNode);
	    				parentJson.put(childrenPropertyName, children);
	    			}
	    			treePath.push(treeNode);
	    			jsonPath.push(jsonNode);
	    			
	    			pathSum ++;
	    			break;
	    		}else{
	    			treePath.pop();
	    			jsonPath.pop();
	    			pathSum --;
	    		}
    		}
    	}
    	return jsonTree;
    }
    
    /**
     * 将列表转换为tree结构的json
     * @param treeList 待排序的List
     * @param c  比较算法，需要实现接口 ListOpt.ParentChild<T>
     * @param childrenPropertyName 为孩子的 属性名
     * @return
     */
    public static <T> JSONArray srotAsTreeAndToJSON
		(List<T> treeList,ParentChild<? super T> c, String childrenPropertyName){
    	 sortAsTree(treeList,c);
    	 return treeToJSONArray(treeList,c,childrenPropertyName);
    }
    
    /**
	 * 将数组结构按照树形展示的形式进行排序，将所有孩子元素放到父元素的下面
	 * 深度优先的排序
	 * @param list
	 * @param c
	 */
	public static <T> List<TreeNode<T>> storedAsTree(List<T> list, ParentChild<? super T> c) {

		List<TreeNode<T>> treeList = new ArrayList<TreeNode<T>>();
		for(T m : list){
			 treeList.add(new TreeNode<T>(m));
		}
		for(TreeNode<T> cNode : treeList){
			for(TreeNode<T> pNode : treeList){
				if(pNode != cNode && c.parentAndChild(pNode.getValue(),cNode.getValue())){
					pNode.addChild(cNode);
					break;
				}
			}
		}
		List<TreeNode<T>> resList = new ArrayList<TreeNode<T>>();
		for(TreeNode<T> node : treeList){
			if(node.isRoot())
				resList.add(node);
		}
		return resList;
	}
	
	  /**
     * 将TreeList转换为JSONArray
     * @param treeList 需要排序的对象列表 必须是 List 
     * @param c  比较算法，需要实现接口 ListOpt.ParentChild<T>
     * @param childrenPropertyName 为孩子的 属性名
     * @return
     */
    public static <T> JSONArray treeToJSONArray
    		(List<TreeNode<T>> treeList, String childrenPropertyName){
    	if(treeList==null || treeList.size()==0)
    		return null;

		JSONArray ja = new JSONArray();
		for(TreeNode<T> c: treeList){
			ja.add( c.toJSONObject(childrenPropertyName));
		}
		return ja;
    }
    
    /**
     * 将列表转换为tree结构的json
     * 和 srotAsTreeAndToJSON 用不同的算法实现，这个需要额外的空间，用递归实现。 
     * @param treeList 待排序的List
     * @param c  比较算法，需要实现接口 ListOpt.ParentChild<T>
     * @param childrenPropertyName 为孩子的 属性名
     * @return
     */
    public static <T> JSONArray srotAsTreeAndToJSON2
		(List<T> treeList,ParentChild<? super T> c, String childrenPropertyName){
    	
    	List<TreeNode<T>> sortTree = storedAsTree(treeList,c);
    	return treeToJSONArray(sortTree,childrenPropertyName);
    }
    
    /**
     * 克隆 一个 list
     * @param souList
     * @return
     */
    public static <T> List<T> cloneList(List<T> souList){
    	if(souList==null)
    		return null;
    	ArrayList<T> deslist = new ArrayList<T>(souList.size()+1);
    	deslist.addAll(souList);
    	return deslist;    	
    }
    /**
     * 对比两个列表，判断哪些需要新增、哪些需要删除、哪些需要更新
     * @param oldList
     * @param newList
     * @param compare
     * @return insert<T> update(old,new)<T,T> delete<T>
     */
    public static <T> Triple<List<T>, List<Pair<T,T>>, List<T>> 
    	compareTwoList(List<T> oldList,List<T> newList,Comparator<T> compare){
    	if(oldList==null ||oldList.size()==0)
    		return new ImmutableTriple<List<T>, List<Pair<T,T>>, List<T>> (
    				newList,null,null);
    	if(newList==null ||newList.size()==0)
    		return new ImmutableTriple<List<T>, List<Pair<T,T>>, List<T>> (
    				null,null,oldList);
    	List<T> souList = cloneList(oldList);
    	List<T> desList = cloneList(newList);
    	Collections.sort(souList, compare);
    	Collections.sort(desList, compare);
    	//---------------------------------------
    	int i=0; int sl = souList.size();
    	int j=0; int dl = desList.size();
    	List<T> insertList = new ArrayList<T>();
    	List<T> delList = new ArrayList<T>();
    	List<Pair<T,T>> updateList = new ArrayList<Pair<T,T>>();
    	while(i<sl&&j<dl){
    		int n = compare.compare(souList.get(i), desList.get(j));
    		if(n<0){
    			delList.add(souList.get(i));
    			i++;
    		}else if(n==0){
    			updateList.add( new ImmutablePair<T,T>(souList.get(i),desList.get(j)));
    			i++;
    			j++;
    		}else /*if(n>0)*/{
    			insertList.add(desList.get(j));
    			j++;
    		}
    	}
    	
    	while(i<sl){
    		delList.add(souList.get(i));
			i++;
    	}
    	
    	while(j<dl){
    		insertList.add(desList.get(j));
			j++;
    	}
    	
    	return new ImmutableTriple<List<T>, List<Pair<T,T>>, List<T>> (
    			insertList,updateList,delList);
    }
    
    /*public static <T> T[] listToArray(List<T> t,Class<T> clazz){    	     	 
    	if(t==null || t.size()==0)
    		return null;
    	@SuppressWarnings("unchecked")
		T[] ta =(T[]) Array.newInstance(clazz, t.size());
    	for(int i=0;i<t.size();i++)
    		ta[i] = t.get(i);
    	return ta;
    }*/
    /**
     * 将list 转换为数组， list.toArray(T[]) 感觉不太好用，要new一个接受的数组对象
     * @param listObj
     * @return
     */
    public static <T> T[] listToArray(List<T> listObj){    	     	 
    	if(listObj==null || listObj.size()==0)
    		return null;
    	@SuppressWarnings("unchecked")
		T[] ta =(T[]) Array.newInstance(listObj.get(0).getClass(), listObj.size());
    	for(int i=0;i<listObj.size();i++)
    		ta[i] = listObj.get(i);
    	return ta;
    }
    
    public static <T> List<T> arrayToList(T[] arrayObj ){    	     	 
    	if(arrayObj==null || arrayObj.length==0)
    		return null;
    	List<T> listObj = new ArrayList<T>(arrayObj.length);
    	for(T obj:arrayObj)
    		listObj.add(obj);
    	return listObj;
    }
    
}
