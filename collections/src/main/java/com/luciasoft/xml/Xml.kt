package com.luciasoft.xml

import org.w3c.dom.Document
import java.io.File
import java.io.FileWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun saveXml(doc: Document, filePath: String)
{
    with (TransformerFactory.newInstance().newTransformer())
    {
        setOutputProperty(OutputKeys.INDENT, "yes")
        setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        transform(DOMSource(doc), StreamResult(FileWriter(filePath)))
    }
}

fun loadXmlFile(filePath: String): Document
{
    return DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder()
        .parse(File(filePath))
}

fun createXmlDocument(rootElementName: String?): Document
{
    val doc = DocumentBuilderFactory
        .newInstance().newDocumentBuilder().newDocument()
    if (rootElementName != null)
    {
        val root = doc.createElement(rootElementName)
        doc.appendChild(root)
    }
    return doc
}