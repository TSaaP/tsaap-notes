package org.tsaap.resources

class ResourceDescription {

  String url
  String metadata

  static constraints = {
    url url: true, unique: true
    metadata nullable: true
  }
}
