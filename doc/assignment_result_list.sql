select
  assignment.`title` as assignment_title,
  `statement`.title as question_title,
  learner.username,
  response.explanation as contribution,
  grader.username as grader,
  peer_grading.`grade` as grade
from
  assignment,
  `sequence`,
  `statement`,
  user as learner,
  `interaction`,
  `choice_interaction_response` as response,
  `peer_grading`,
  user as grader
where
  `assignment`.id IN (369,388) and
  `assignment`.id = `sequence`.`assignment_id` and
  statement.id = `sequence`.`statement_id` and
  response.learner_id = learner.id and
  response.interaction_id = interaction.id and
  interaction.sequence_id = `sequence`.id and
  response.attempt = 2 and
  `peer_grading`.`grader_id` = grader.id and
  `peer_grading`.`response_id` = response.id
order by assignment.id, sequence.id, learner.username