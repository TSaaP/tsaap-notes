---
layout: post
title:  "How to configure and launch Tsaap-Notes as an external tool from Moodle?"
date:   2017-01-22 14:43:45 +0200
author: Franck Silvestre
categories: lti Moodle
---

### 1. Configuring and Launching an activity corresponding to an assignment

As a pre-requisite for this documentation, it is assumed that you read the Moodle documentation on the configuration 
of an external tool.

- [Moodle 2.9](https://docs.moodle.org/29/en/External_tool_settings)
- [Moodle 3.1](https://docs.moodle.org/31/en/External_tool_settings)

In all cases, the launch URL for a Tsaap-Notes activity is:

`https://notes.tsaap.eu/tsaap-notes/launch`

# 1.1 Creation "on the fly" of the corresponding assignment

In this case, you don't have created the assignment in Tsaap-Notes. 
You have to specify, in your configuration, a custom parameter indicating that you want to launch an activity of type assignment:

`assignment=true`

The screenshot below shows the Moodle form whith the "custom parameters" field.

![Moodle form]({{ site.baseurl }}/assets/screenshotMoodle1.png)

# 1.2 Linkink to an existing assignment

In Tsaap-Notes, when an assignment is created, a unique global id is associated with the created assignment.
The screenshot below shows an assignment In tsaap-notes with its associated global unique id.
 
![Assignment and its id]({{ site.baseurl }}/assets/AssignmentGlobalId.png) 

In the case you want to link your moodle activity with an existing assignment, you have to provide Moodle with the global id.
You have to specify, in your configuration, a custom parameter indicating the global id of the assignment:

`assignmentid=<the global id of the assignment>`

The screenshot below shows the Moodle form whith the "custom parameters" field.

![Moodle form]({{ site.baseurl }}/assets/screenshotMoodle2.png)