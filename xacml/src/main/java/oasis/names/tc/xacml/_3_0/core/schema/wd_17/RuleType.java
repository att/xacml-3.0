//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.05.23 at 02:47:00 PM EDT 
//


package oasis.names.tc.xacml._3_0.core.schema.wd_17;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RuleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RuleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}Description" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}Target" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}Condition" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}ObligationExpressions" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}AdviceExpressions" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="RuleId" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Effect" use="required" type="{urn:oasis:names:tc:xacml:3.0:core:schema:wd-17}EffectType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RuleType", propOrder = {
    "description",
    "target",
    "condition",
    "obligationExpressions",
    "adviceExpressions"
})
public class RuleType {

    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "Target")
    protected TargetType target;
    @XmlElement(name = "Condition")
    protected ConditionType condition;
    @XmlElement(name = "ObligationExpressions")
    protected ObligationExpressionsType obligationExpressions;
    @XmlElement(name = "AdviceExpressions")
    protected AdviceExpressionsType adviceExpressions;
    @XmlAttribute(name = "RuleId", required = true)
    protected String ruleId;
    @XmlAttribute(name = "Effect", required = true)
    protected EffectType effect;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link TargetType }
     *     
     */
    public TargetType getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link TargetType }
     *     
     */
    public void setTarget(TargetType value) {
        this.target = value;
    }

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link ConditionType }
     *     
     */
    public ConditionType getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConditionType }
     *     
     */
    public void setCondition(ConditionType value) {
        this.condition = value;
    }

    /**
     * Gets the value of the obligationExpressions property.
     * 
     * @return
     *     possible object is
     *     {@link ObligationExpressionsType }
     *     
     */
    public ObligationExpressionsType getObligationExpressions() {
        return obligationExpressions;
    }

    /**
     * Sets the value of the obligationExpressions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ObligationExpressionsType }
     *     
     */
    public void setObligationExpressions(ObligationExpressionsType value) {
        this.obligationExpressions = value;
    }

    /**
     * Gets the value of the adviceExpressions property.
     * 
     * @return
     *     possible object is
     *     {@link AdviceExpressionsType }
     *     
     */
    public AdviceExpressionsType getAdviceExpressions() {
        return adviceExpressions;
    }

    /**
     * Sets the value of the adviceExpressions property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdviceExpressionsType }
     *     
     */
    public void setAdviceExpressions(AdviceExpressionsType value) {
        this.adviceExpressions = value;
    }

    /**
     * Gets the value of the ruleId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * Sets the value of the ruleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleId(String value) {
        this.ruleId = value;
    }

    /**
     * Gets the value of the effect property.
     * 
     * @return
     *     possible object is
     *     {@link EffectType }
     *     
     */
    public EffectType getEffect() {
        return effect;
    }

    /**
     * Sets the value of the effect property.
     * 
     * @param value
     *     allowed object is
     *     {@link EffectType }
     *     
     */
    public void setEffect(EffectType value) {
        this.effect = value;
    }

}
