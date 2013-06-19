package org.tsaap.notes

class Tag {

  String name

  void setName(String name) {
    this.@name = name.toLowerCase()
  }

  static constraints = {
    name blank: false, unique: true
  }

  static mapping = {
    cache 'read-write'
  }
}
