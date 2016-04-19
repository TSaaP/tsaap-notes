select 
	last_name, first_name, username, lti_user_id, lti_consumer_key, 
	response_1.answer_list_as_string as first_answer, note_1.content as first_explanation, 
	response_1.confidence_degree as first_confidence_degree, response_1.`percent_credit` as first_score,
	response_2.answer_list_as_string as second_answer, note_2.content as second_explanation, 
	response_2.confidence_degree as second_confidence_degree, response_2.`percent_credit` as second_score
from 
	user, `lms_user`, 
	`live_session_response` as response_1, `note` as note_1, 
	`live_session_response` as response_2, `note` as note_2  
where 
	user.id =  `lms_user`.`tsaap_user_id` and
	user.id = response_1.`user_id` and
	response_1.`session_phase_id` = 374 and
	note_1.id = response_1.`explanation_id` and
	user.id = response_2.`user_id` and
	response_2.`session_phase_id` = 375 and
	note_2.id = response_2.`explanation_id`
	