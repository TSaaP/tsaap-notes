package org.tsaap

import org.tsaap.directory.User
import org.tsaap.notes.Context
import org.tsaap.notes.ContextService
import org.tsaap.notes.Note
import org.tsaap.notes.NoteService

class BootstrapTestService {

    NoteService noteService
    ContextService contextService

    User learnerPaul
    User teacherJeanne
    User learnerMary
    List<User> learners = []
    Note note1
    Note note2
    Context context1
    Context context2


    def initializeTests() {
        initializeUsers()
        initializeNotes()
        initializeContexts()
    }

    def initializeUsers() {
        learnerPaul = User.findByUsername("learner_paul")
        if (!learnerPaul) {
            learnerPaul = new User(firstName: "Paul", lastName: "D", username: "learner_paul", password: "password", email: "paul@nomail.com", language: 'fr').save()
        }
        teacherJeanne = User.findByUsername("teacher_jeanne")
        if (!teacherJeanne) {
            teacherJeanne = new User(firstName: "Jeanne", lastName: "L", username: "teacher_jeanne", password: "password", email: "jeanne@nomail.com",language: 'fr').save()
        }
        learnerMary = User.findByUsername("learner_Mary")
        if (!learnerMary) {
            learnerMary = new User(firstName: "Mary", lastName: "S", username: "learner_Mary", password: "password", email: "mary@nomail.com",language: 'fr').save()
        }
        for (int i=0 ; i<10 ; i++) {
            User currentUser = User.findByUsername("learner_$i")
            if(!currentUser) {
                currentUser = new User(firstName: "learner_$i", lastName: "learner_$i", username: "learner_$i", password: "learner_$i", email: "learner_$i@nomail.com", language: 'en').save()
            }
            learners[i] = currentUser
        }
    }

    def initializeNotes(){
        if(!Note.findByContent("content note 1")){
            note1 = noteService.addNote(learnerPaul,"content note 1")
        }
        if(!Note.findByContent("content note 2")) {
            note2 = noteService.addNote(learnerMary,"content note 2")
        }
    }

    def initializeContexts(){
        if(!Context.findByContextName("Context1")){
            context1 = contextService.saveContext(new Context(owner: learnerPaul, contextName: "Context1", url: 'http://www.w3.org', descriptionAsNote: 'a description'))
        }
        if(!Context.findByContextName("Context2")){
            context2 = contextService.saveContext(new Context(owner: learnerPaul, contextName: "Context2", url: 'http://www.w3.org', descriptionAsNote: 'a description'))
        }

    }
}
