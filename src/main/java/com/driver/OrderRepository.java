package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here

        String id=order.getId();
        orderMap.put(id,order);


    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        DeliveryPartner partner=new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,partner);

        partnerToOrderMap.put(partnerId,new HashSet<String>());

    }

    public void saveOrderPartnerMap(String orderId, String partnerId){

        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order

            orderToPartnerMap.put(orderId,partnerId);

           DeliveryPartner dp=partnerMap.get(partnerId);
           dp.setNumberOfOrders(dp.getNumberOfOrders()+1);

            partnerToOrderMap.get(partnerId).add(orderId);

        }
    }

    public Order findOrderById(String orderId){
        // your code here
         Order order=null;
         if(orderMap.containsKey(orderId)){
             order=orderMap.get(orderId);
         }
        return order;
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here

        DeliveryPartner deliveryPartner=null;

        if(partnerMap.containsKey(partnerId)){
            deliveryPartner=partnerMap.get(partnerId);
        }
        return deliveryPartner;
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here

        Integer ans=0;
        if(partnerToOrderMap.containsKey(partnerId)){
          ans=partnerToOrderMap.get(partnerId).size();
        }

        return ans;
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here

        HashSet<String>st=partnerToOrderMap.get(partnerId);
        List<String>ans=new ArrayList<>();

        for(String val:st){
            ans.add(val);
        }
        return ans;
    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        List<String>ans=new ArrayList<>();

        for(String val:orderMap.keySet()){
            ans.add(val);
        }
        return ans;
    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID

        if(partnerMap.containsKey(partnerId)){

//            DeliveryPartner dp=partnerMap.get(partnerId);
//            dp.setNumberOfOrders(dp.getNumberOfOrders()-1);

            partnerMap.remove(partnerId);

            for(String v:orderToPartnerMap.keySet()){
                if(orderToPartnerMap.get(v)==partnerId){

                    orderToPartnerMap.remove(v);
                    break;
                }
            }
            partnerToOrderMap.remove(partnerId);
        }
    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID

       if(orderMap.containsKey(orderId)) {
           orderMap.remove(orderId);
           orderToPartnerMap.remove(orderId);

           for(String st:partnerToOrderMap.keySet()){
               if(partnerToOrderMap.get(st).contains(orderId)){
                   partnerToOrderMap.remove(st);
                   break;
               }
           }
       }

    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        int cnt=0;

        for(String st:orderMap.keySet()){
            if(!orderToPartnerMap.containsKey(st)){
                cnt++;
            }
        }
        return cnt;
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here

        int ans=0;
        String[] arr=timeString.split(":");
        int gt=Integer.parseInt(arr[0])*60+Integer.parseInt(arr[1]);

        HashSet<String>hs=partnerToOrderMap.get(partnerId);

        for(String orderid:hs){
            Order order=orderMap.get(orderid);

            if(orderid!=null && gt<order.getDeliveryTime()){
                ans++;
            }
        }
        return ans;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM

        int latestTime = Integer.MIN_VALUE;
        String lastDeliveryTime = "";
        int time = 0;
        Set<String> partnerOrders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<>());
        for (String orderId : partnerOrders) {
            Order order = orderMap.get(orderId);
            if (order != null && order.getDeliveryTime() > latestTime) {
                latestTime = order.getDeliveryTime();
                time = order.getDeliveryTime();
            }
        }
        int hours = time / 60;
        int minutes = time % 60;
        lastDeliveryTime = String.format("%02d:%02d", hours, minutes);
        return lastDeliveryTime;
        
    }
}