package com.example.personalhealthcare;

public class doctorlist {

	String docname;
	String docdes;
	String docdis;
	String docspes;

	public doctorlist(String docname, String docdes, String docdis, String docspes) {
		this.docname = docname;
		this.docdes = docdes;
		this.docdis = docdis;
		this.docspes = docspes;
	}

	public String getDocname() {
		return docname;
	}

	public void setDocname(String docname) {
		this.docname = docname;
	}

	public String getDocdes() {
		return docdes;
	}

	public void setDocdes(String docdes) {
		this.docdes = docdes;
	}

	public String getDocspes() {
		return docspes;
	}

	public void setDocspes(String docspes) {
		this.docspes = docspes;
	}

	public String getDocdis() {
		return docdis;
	}

	public void setDocdis(String docdis) {
		this.docdis = docdis;
	}
}
