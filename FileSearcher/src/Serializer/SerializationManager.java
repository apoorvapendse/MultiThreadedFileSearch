package Serializer;

import Indexer.DirNode;

import java.io.*;

public class SerializationManager
{

    public void serialize(DirNode root,String path)
    {
        File f = new File(path);
        try{
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(root);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public DirNode deserialize(String path)
    {
        File f = new File(path);
        try{
            FileInputStream fileInputStream = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            DirNode root =(DirNode) objectInputStream.readObject();
            return root;
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
