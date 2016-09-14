package xiaoliang.ltool.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.List;

/**
 * Created by LiuJ on 2016/9/13.
 * Xml的封装工具
 */
public class XMLUtil {
    private Element root;

    public XMLUtil(String xml) throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        root = document.getRootElement();
    }

    /**
     * 获取根节点的键值
     * @param name
     * @return
     */
    public String getText(String name){
        return getText(root,name);
    }

    /**
     * 获取制定节点下的键值
     * @param element
     * @param name
     * @return
     */
    public String getText(Element element,String name){
        return element.element(name).getText();
    }

    /**
     * 获取指定节点下的集合
     * @param element
     * @param name
     * @return
     */
    public List<Element> getList(Element element,String name){
        return element.elements(name);
    }

    /**
     * 获取指定节点下的节点
     * @param element
     * @param name
     * @return
     */
    public Element getElement(Element element,String name){
        return element.element(name);
    }

    /**
     * 获取根节点下的集合
     * @param name
     * @return
     */
    public List<Element> getList(String name){
        return root.elements(name);
    }

    /**
     * 获取根节点下的节点
     * @param name
     * @return
     */
    public Element getElement(String name){
        return root.element(name);
    }


}
