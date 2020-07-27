/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terraui1;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Nate
 */
public class LoadSaveFileManager implements java.io.Serializable{


    protected static void saveState(){
        List<HashMap> list = new ArrayList<>();
        list.add(ComponentManager.internalFramePara);
        list.add(ComponentManager.componentPara);
        try {
            String path = new File(".").getCanonicalPath();
            File dir = new File(path + "/src/sve");
            if (!dir.exists()){
                boolean bool = dir.mkdirs();
                if (bool) {
                    System.out.println("New directory has been added to:" + path);
                } else {
                    System.out.println("Create directory error:");
                }       
            }

            File state = new File(path + "/src/sve/save");

            try (FileOutputStream fos = new FileOutputStream(state); ObjectOutputStream oos = new ObjectOutputStream(fos)) {               
                oos.writeObject(list);
                oos.flush();
                oos.close();
                fos.close();
            }
            
            
        }catch (IOException e){System.out.println(e);}
    }
    
    protected static void loadState(){
        try{
            String path = new File(".").getCanonicalPath();
            File saveFile = new File(path + "/src/sve/save");   
            if (saveFile.exists()){
                FileInputStream fis = new FileInputStream(saveFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                List<HashMap> list = (List<HashMap>) ois.readObject();
                
                ois.close();
                fis.close();
                
                HashMap <String, List<String>> loadCompPara = (HashMap <String, List<String>>) list.get(1);
                HashMap <String, String> loadiFramePara = (HashMap <String, String>) list.get(0);
                ComponentManager.componentPara.clear();
                ComponentManager.componentPara.putAll(loadCompPara);
                ComponentManager.internalFramePara.clear();
                ComponentManager.internalFramePara.putAll(loadiFramePara);
                         //Collections.sort(ComponentManager.internalFramePara);
                TreeMap <String, String> sorted = new TreeMap<> (loadiFramePara);
                Set<Entry<String, String>> sortedEntries = sorted.entrySet();
                
                
                for (Entry<String,String> m: sortedEntries){
                    System.out.println(m.getKey()+" : "+m.getValue());
                    FunctionManager.addPanel(m.getValue());
                }
                
                for (Map.Entry<String, List<String>> m : ComponentManager.componentPara.entrySet()) {
                    System.out.println(m.getKey() + " : " + m.getValue());
                    
                    List<String> cPara = (List<String>) m.getValue();
                    
                    String img = cPara.get(0);
                    String xPos = cPara.get(1);
                    String yPos = cPara.get(2);
                    String cWidth = cPara.get(3);
                    String cHeight = cPara.get(4);
                    String cText = cPara.get(5);
                    String cCmd = cPara.get(6);
                    String cLayer = cPara.get(7);
                    String cFrame = cPara.get(8);
                    String cls = cPara.get(9); 
                    String id = cPara.get(10);
                    
                    
                    if ("class terraui1.CustomButton".equals(cls)){
                        //System.out.println(cls);
                        FunctionManager.loadUI_CustomButton(img, xPos, yPos, cWidth, cHeight, cText, cCmd, cLayer, cFrame, id);
                    }else if ("class terraui1.CustomImageBox".equals(cls)){
                        FunctionManager.loadUI_ImageBox(img, xPos, yPos, cWidth, cHeight, cLayer, cFrame, id);
                        
                    }
                    
                }
                ComponentManager.desktopPane.updateUI();
            }
        }catch (Exception e) {}
        
    }

}
/*
addButton(String imgDir, String xPos, String yPos, String cWidth, String cHeight, String cTitle, String cCmd, String cLayer, String cFrame, String id) {
        compPara.add(imgIcon.toString()); //0
        compPara.add(Integer.toString(xPos)); //1
        compPara.add(Integer.toString(yPos)); //2
        compPara.add(Integer.toString(cWidth)); //3
        compPara.add(Integer.toString(cHeight)); //4
        compPara.add(text); //5
        compPara.add(cmd); //6
        compPara.add(cLayer); //7
        compPara.add(cFrame); //8
        compPara.add(cls); //9
        compPara.add(id); //10
*/