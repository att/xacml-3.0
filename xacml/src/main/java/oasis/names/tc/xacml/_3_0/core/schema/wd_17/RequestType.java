//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.23 at 02:47:00 PM EDT 
//


package oasis.names.tc.xacml._3_0.core.schema.wd_17;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RequestType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}RequestDefaults" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}Attributes" maxOccurs="unbounded"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}MultiRequests" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ReturnPolicyIdList" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="CombinedDecision" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RequestType", propOrder = {
    "requestDefaults",
    "attributes",
    "multiRequests"
})
public class RequestType {

    @XmlElement(name = "RequestDefaults")
    protected RequestDefaultsType requestDefaults;
    @XmlElement(name = "Attributes", required = true)
    protected List<AttributesType> attributes;
    @XmlElement(name = "MultiRequests")
    protected MultiRequestsType multiRequests;
    @XmlAttribute(name = "ReturnPolicyIdList", required = true)
    protected boolean returnPolicyIdList;
    @XmlAttribute(name = "CombinedDecision", required = true)
    protected boolean combinedDecision;

    /**
     * Gets the value of the requestDefaults property.
     * 
     * @return
     *     possible object is
     *     {@link RequestDefaultsType }
     *     
     */
    public RequestDefaultsType getRequestDefaults() {
        return requestDefaults;
    }

    /**
     * Sets the value of the requestDefaults property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestDefaultsType }
     *     
     */
    public void setRequestDefaults(RequestDefaultsType value) {
        this.requestDefaults = value;
    }

    /**
     * Gets the value of the attributes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributesType }
     * 
     * 
     */
    public List<AttributesType> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<AttributesType>();
        }
        return this.attributes;
    }

    /**
     * Gets the value of the multiRequests property.
     * 
     * @return
     *     possible object is
     *     {@link MultiRequestsType }
     *     
     */
    public MultiRequestsType getMultiRequests() {
        return multiRequests;
    }

    /**
     * Sets the value of the multiRequests property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiRequestsType }
     *     
     */
    public void setMultiRequests(MultiRequestsType value) {
        this.multiRequests = value;
    }

    /**
     * Gets the value of the returnPolicyIdList property.
     * 
     */
    public boolean isReturnPolicyIdList() {
        return returnPolicyIdList;
    }

    /**
     * Sets the value of the returnPolicyIdList property.
     * 
     */
    public void setReturnPolicyIdList(boolean value) {
        this.returnPolicyIdList = value;
    }

    /**
     * Gets the value of the combinedDecision property.
     * 
     */
    public boolean isCombinedDecision() {
        return combinedDecision;
    }

    /**
     * Sets the value of the combinedDecision property.
     * 
     */
    public void setCombinedDecision(boolean value) {
        this.combinedDecision = value;
    }

}
