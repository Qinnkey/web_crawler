package com.huisou.crawler;

import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.bcel.generic.IINC;
import org.apache.bcel.generic.NEW;
import org.apache.commons.collections.map.StaticBucketMap;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.Index;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

import javax.swing.border.TitledBorder;

  
/** 
* @author qinkai 
* @date 2017年8月31日
* @return 小米商城数据
*/
// 数据抓取:method 1
// jsoup + phantomjs
public class Phantomjs { 
	  // 加载phantomjs.exe
      public static String getAjaxCotnent(String url) throws IOException {  
            Runtime rt = Runtime.getRuntime();
            String exec = "e:/DownLoad/phantomjs/phantomjs-2.1.1-windows/phantomjs-2.1.1-windows/bin/phantomjs.exe "
            		+ "e:/DownLoad/phantomjs/phantomjs-2.1.1-windows/phantomjs-2.1.1-windows/bin/codes.js "+url;
            Process p = rt.exec(exec);
            InputStream is = p.getInputStream();  
            BufferedReader br = new BufferedReader(new InputStreamReader(is));  
            StringBuffer sbf = new StringBuffer();  
            String tmp = "";  
            while((tmp = br.readLine())!=null){  
                sbf.append(tmp);  
            }  
            
            return sbf.toString();  
        }  
      // 正则表达式匹配
      public static String matchResult(Pattern p,String str){  
          StringBuilder sb = new StringBuilder();  
          Matcher m = p.matcher(str);  
          while (m.find())
          for (int i = 0; i <= m.groupCount(); i++)   
          {  
              sb.append(m.group());     
          }  
          return sb.toString();  
      }  
      // 是否包含中文
      public static boolean isContainChinese(String str) {

          Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
          Matcher m = p.matcher(str);
          if (m.find()) {
              return true;
          }
          return false;
      }
      // 导出数据库数据到excel
      public static void writeDataToExcel() throws Exception{
    	InsertDB insertDB = new InsertDB();
      	insertDB.connSQL();
      	String query = "select * from mibrands";
      	ResultSet result = insertDB.selectSQL(query);
      	
      	List<Map> list = new ArrayList<Map>();
      	list.clear();
      	String title[] = {"商品名称","商品价格","全部评价","满意人数","满意度"};
      	DownLoadutils.createExcel("E:/MiPrice.xls", "sheet1", title);
      	
      	while (result.next()) {
      		String brand = result.getString("brands");                                // 商品名称
      		String price = result.getString("price");                                // 商品价格
      		String allEval = result.getString("total");                              // 商品全部评价
      		String satisfiedNum = result.getString("satisfiedNum");                         // 商品满意人数
      		String satisfaction = result.getString("satisfaction");                         // 商品满意度

      		Map<String,String> map=new HashMap<String,String>();
      		map.put("商品名称", brand);
      		map.put("商品价格", price);
      		map.put("全部评价", allEval);
      		map.put("满意人数", satisfiedNum);
           	map.put("满意度", satisfaction); 
           	list.add(map);        		
//      		System.err.println("商品名称: " + brand);
//      		System.err.println("商品价格: " + price);
//      		System.err.println("商品全部评价: " + allEval);
//      		System.err.println("商品满意人数: " + satisfiedNum);
//      		System.err.println("商品评价满意度: " + satisfaction);
//      		System.err.println("************************");
			}
         DownLoadutils.writeToExcel("E:/MiPrice.xls", "sheet1", list);
      }
//        public static void main(String[] args) throws Exception {
        	
//        	writeDataToExcel();
        
        
        
//            List<Map> list = new ArrayList<Map>();
//            list.clear();
//            String title[] = {"商品名称","商品价格","全部评价","满意人数","满意度"};
//            DownLoadutils.createExcel("E:/MiPrice.xls", "sheet1", title);
        	
//        	// 抓取数据  /*
//            System.err.println("begin to add data to mysql..................... \n");
//            String brand = "";                                // 商品名称
//            String price = "";                                // 商品价格
//            String allEval = "";                              // 商品全部评价
//            String satisfiedNum = "";                         // 商品满意人数
//            String satisfaction = "--";                         // 商品满意度
//            String baseURL = "https://list.mi.com/0-0-0-0-0-0-0-0-"; // 基类URL
//        	for (int ii = 10; ii <= 19; ii++) {
//        		String URL = baseURL + String.valueOf(ii);
//
//        		String miHtml = getAjaxCotnent(URL); // 取商品详情url
//                Document miParse = Jsoup.parse(miHtml);
//                Elements element = miParse.select(".goods-list"); // 单个界面所有商品
//                int size = element.get(0).children().size();   
//                for (int i = 0; i < size; i++) {
//                	Element element2 = element.get(0).child(i);    // goods-item foreach
//                    Element eleStr = element2.select("a").first(); // href
//                    String url = eleStr.attr("href");
//                    if (!url.contains("https:")){
//                    	url = "https:" + url;                       // 拼接绝对路径
//                    } 
//                    brand = element2.child(2).text();
//                    price = element2.child(3).ownText().replaceAll("[\u4e00-\u9fa5]+", "").trim();
//           
//                    String goodsHtml = getAjaxCotnent(url);        // 商品详情url
//                    Document goodsParse = Jsoup.parse(goodsHtml);
//                    Elements element3 = goodsParse.select("#J_headNav > div:nth-child(1) > div:nth-child(2)");   // 抓取 div class = con
//                    if (element3.size() == 0 && goodsParse.select("b.J_mi_goodsPrice").size() != 0){
//                    	// 商品详情页中没有用户评价         	
//                    	price = goodsParse.select("b.J_mi_goodsPrice").text().replaceAll("[\u4e00-\u9fa5]+", "").trim();
//                    	allEval = goodsParse.select("li.J_scrollHref:nth-child(1) > b:nth-child(2)").text();
//                    	satisfaction = goodsParse.select("li.J_scrollHref:nth-child(3) > b:nth-child(2)").text();
//                    	if (!allEval.equals("0") && !satisfaction.equals("--")){
//                    		if (isContainChinese(allEval) && matchResult(Pattern.compile("[\u4e00-\u9fa5]"),allEval).equals("万")) {
//                    			String chinese = allEval.replaceAll("[\u4e00-\u9fa5]+", "").trim();
//                    			allEval = String.valueOf((int)(Float.parseFloat(chinese) * 10000 + 0.5));
//                    		}
//                    		float percent=new Float(satisfaction.substring(0,satisfaction.indexOf("%")))/100;
//                    		int num = (int)(Integer.parseInt(allEval) * percent + 0.5);
//                    		satisfiedNum = String.valueOf(num);
//                    	} 
//                    } else if (element3.size() != 0){
//                    	if (price.equals("")){
//                    		price = matchResult(Pattern.compile("[0-9]"),goodsParse.select("div.price").text()).trim();
//                        }
//                        
//                        Elements elements = element3.get(0).children().last().children(); // con 下有一个或者两个div 取最后一个div
//                        String commentUrl = "";
//                        for(Element ee:elements){
//                        	String titleName = ee.text();
//                        	if("用户评价".equals(titleName)){
//                        		String string = ee.select("a").first().attr("href");
//                        		if (!string.contains("https:")){
//                        			commentUrl = "https:" + ee.select("a").first().attr("href"); // 商品用户评价url
//                        		}
//                        		break;
//                        	}
//                        }
//                        
//                        // 商品详情页面
////                        System.err.println("URL : " + commentUrl);
//                        String commentHtml = getAjaxCotnent(commentUrl); // 商品用户评价url
////                        System.err.println("++++++++ " + commentHtml);
//                        Document commentParse = Jsoup.parse(commentHtml);
//                        
////                        String text = commentParse.select(".m-comment-nav > h2:nth-child(1)").get(0).text(); // 大家认为
//                        // 没有用户评价
//                        if (commentParse.select(".m-num").size() > 0 && commentParse.select(".percent").size() > 0 ){
//                        	Elements select1 = commentParse.select(".m-num");
//                            satisfiedNum = select1.get(0).text();
//                            Elements select2 = commentParse.select(".percent");
//                            satisfaction = select2.get(0).text().replaceAll("[\u4e00-\u9fa5]+", "").trim();
//                            
//                            Elements select = commentParse.select("a.item:nth-child(1)"); 
//                            if (select.size() == 0){
//                            	if (!satisfaction.equals("") && !satisfiedNum.equals("")){
//                                	float percent=new Float(satisfaction.substring(0,satisfaction.indexOf("%")))/100;
//                            		int num = (int)(Integer.parseInt(satisfiedNum) / percent + 0.5);
//                            		allEval = String.valueOf(num);
//                            	}
//                            } else {
//                            	allEval = matchResult(Pattern.compile("[0-9]"),select.get(0).text());
//        					}
//                        } else {
//        					allEval = "0";
////        					satisfiedNum = "0";
//        					satisfaction = "--";
//                        }
//                        
//                        
//    				} else {
//    					allEval = "0";
//    					satisfiedNum = "0";
//    					satisfaction = "--";
//    				}
//                          
//                    // 输出商品信息
//                    if (allEval.equals("0") || satisfaction.equals("--")){
//                    	satisfiedNum = "0";
//                    }
////                    System.err.println("商品名称: " + brand);
////                    System.err.println("商品价格: " + price);
////                    System.err.println("商品全部评价: " + allEval);
////                    System.err.println("商品满意人数: " + satisfiedNum);
////                    System.err.println("商品评价满意度: " + satisfaction);
////                    System.err.println("************************");
//                    Map<String,String> map=new HashMap<String,String>();
//                    map.put("brand", brand);
//                    map.put("price", price);
//                    map.put("allEval", allEval);
//                    map.put("satisfiedNum", satisfiedNum);
//                    map.put("satisfaction", satisfaction); 
////                    list.add(map);
//                    String sql = "insert into mibrands(brands,price,total,satisfiedNum,satisfaction) values(?,?,?,?,?)";
//                    if (insertDB.insertSQL(sql,map) == true){
//                    	System.out.println("add the " + String.valueOf(i + 1) + "th data success.......................");
//                    }
//    			}
//                System.err.println(String.valueOf(ii) + "th page end.......................");
//        	}
//        	
//        	insertDB.deconnSQL();
////            DownLoadutils.writeToExcel("E:/MiPrice.xls", "sheet1", list);
//            System.err.println("");
//            System.err.println("end to add data to mysql.....................");
//        }  
}
