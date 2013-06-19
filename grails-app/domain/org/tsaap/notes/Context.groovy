package org.tsaap.notes

class Context {

  String url
  String metadata

  static constraints = {
    url url: true
    metadata nullable: true
  }
}
