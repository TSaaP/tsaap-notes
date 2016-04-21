select 
	last_name, first_name, username, lti_user_id, lti_consumer_key, 
	response_1.answer_list_as_string as first_answer, note_1.content as first_explanation, 
	response_1.confidence_degree as first_confidence_degree, response_1.`percent_credit` as first_score,
	response_2.answer_list_as_string as second_answer, note_2.content as second_explanation, 
	response_2.confidence_degree as second_confidence_degree, response_2.`percent_credit` as second_score
from 
	`user` 
		inner join (`live_session_response` as response_1 left join `note` as note_1 on response_1.`explanation_id` = note_1.id) on  user.`id` = response_1.`user_id` 
		left join (`live_session_response` as response_2 left join `note` as note_2 on response_2.`explanation_id` = note_2.id) on user.id = response_2.`user_id`
		left join `lms_user` on user.`id` = `lms_user`.`tsaap_user_id`
where 
	response_1.`session_phase_id` = 360 and
	response_2.`session_phase_id` = 363 
order by
	user.last_name, user.first_name
	
	
select 
	last_name, first_name, username, lti_user_id, lti_consumer_key, 
	response_1.answer_list_as_string as first_answer, note_1.content as first_explanation, 
	response_1.confidence_degree as first_confidence_degree, response_1.`percent_credit` as first_score,
	response_2.answer_list_as_string as second_answer, note_2.content as second_explanation, 
	response_2.confidence_degree as second_confidence_degree, response_2.`percent_credit` as second_score
from 
	`user` 
		inner join (`live_session_response` as response_1 left join `note` as note_1 on response_1.`explanation_id` = note_1.id) on  user.`id` = response_1.`user_id` 
		left join (`live_session_response` as response_2 left join `note` as note_2 on response_2.`explanation_id` = note_2.id) on user.id = response_2.`user_id`
		left join `lms_user` on user.`id` = `lms_user`.`tsaap_user_id`
where 
	response_1.`session_phase_id` = 360 and
	response_2.`session_phase_id` = 363 
order by
	user.last_name, user.first_name
	
	
select 
	last_name, first_name, username, `live_session_response`.id, `live_session_response`.`session_phase_id`
from 
	user, `live_session_response`, `live_session`
where 
user.id = `live_session_response`.`user_id` and
`live_session`.id = `live_session_response`.`live_session_id` and
`live_session_id` = 547
order by
last_name, `first_name`