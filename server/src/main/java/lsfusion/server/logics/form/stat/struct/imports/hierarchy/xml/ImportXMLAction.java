package lsfusion.server.logics.form.stat.struct.imports.hierarchy.xml;

import com.google.common.base.Throwables;
import lsfusion.base.file.RawFileData;
import lsfusion.server.logics.form.stat.struct.hierarchy.xml.XMLNode;
import lsfusion.server.logics.form.stat.struct.imports.hierarchy.ImportHierarchicalAction;
import lsfusion.server.logics.form.struct.FormEntity;
import org.apache.commons.io.input.BOMInputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStreamReader;

public class ImportXMLAction extends ImportHierarchicalAction<XMLNode> {

    public ImportXMLAction(int paramsCount, FormEntity formEntity, String charset, boolean hasRoot, boolean hasWhere) {
        super(paramsCount, formEntity, charset, hasRoot, hasWhere);
    }

    @Override
    public XMLNode getRootNode(RawFileData fileData, String root) {
        try {
            return new XMLNode(findRootNode(fileData, root));
        } catch (JDOMException | IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public Element findRootNode(RawFileData file, String root) throws JDOMException, IOException {
        //if charset is not provided, it's getting from xml header (<?xml version="1.0" encoding="utf-8"?>)
        SAXBuilder builder = new SAXBuilder();
        Document doc = charset != null ? builder.build(new InputStreamReader(new BOMInputStream(file.getInputStream()), charset)) : builder.build(file.getInputStream());
        Element rootNode = findRootNode(doc.getRootElement(), root);
        if(rootNode == null)
            throw new RuntimeException(String.format("Import XML error: root node %s not found", root));
        return rootNode;
    }

    private static Element findRootNode(Element rootNode, String root) {
        if (root == null || rootNode.getName().equals(root))
            return rootNode;
        for (Object child : rootNode.getChildren()) {
            Element found = findRootNode((Element) child, root);
            if (found != null)
                return found;
        }
        return null;
    }
}