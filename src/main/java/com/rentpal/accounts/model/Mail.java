package com.rentpal.accounts.model;

import java.util.Map;

/**
 * The Class Mail.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 12, 2020 10:05:27 PM
 */

public class Mail {

    /** The from. */
    private String from;
    
    /** The to. */
    private String to;
    
    /** The subject. */
    private String subject;
    
    /** The content. */
    private String content;
    
    /** The model. */
    private Map<String, Object> model; 
    
    /** The template. */
    private String template;

    /**
     * Instantiates a new mail.
     *
     * @param from the from
     * @param to the to
     * @param subject the subject
     */
    public Mail(String from, String to, String subject) {
        this.from = from;
        this.to = to;
        this.subject = subject;
    }

    /**
     * Gets the from.
     *
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the from.
     *
     * @param from the new from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gets the to.
     *
     * @return the to
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the to.
     *
     * @param to the new to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * Gets the subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject.
     *
     * @param subject the new subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets the content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content.
     *
     * @param content the new content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the model.
     *
     * @return the model
     */
    public Map<String, Object> getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model the model
	 */
	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 *
	 * @param template the new template
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
}