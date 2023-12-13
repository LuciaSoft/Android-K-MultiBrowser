package com.luciasoft.xml

import org.w3c.dom.Document
import org.xml.sax.SAXException
import java.io.File
import java.io.FileWriter
import java.io.IOException
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

@Throws(TransformerException::class, IOException::class)
fun saveXml(doc: Document, filePath: String)
{
    with(TransformerFactory.newInstance().newTransformer())
    {
        setOutputProperty(OutputKeys.INDENT, "yes")
        setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        transform(DOMSource(doc), StreamResult(FileWriter(filePath)))
    }
}

@Throws(ParserConfigurationException::class, IOException::class, SAXException::class)
fun loadXmlFile(filePath: String): Document
{
    return DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder()
        .parse(File(filePath))
}

@Throws(ParserConfigurationException::class)
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