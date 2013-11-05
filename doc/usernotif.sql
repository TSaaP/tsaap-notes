SELECT tuser.id as user_id, tuser.first_name, tuser.email, tcontext.id as context_id, tcontext.context_name, count(tnote.id) 
FROM `tsaap-notes`.note as tnote 
INNER JOIN `tsaap-notes`.context_follower as tcontextfo ON tnote.context_id = tcontextfo.context_id 
INNER JOIN `tsaap-notes`.context as tcontext ON tcontextfo.context_id = tcontext.id
INNER JOIN `tsaap-notes`.user as tuser ON tcontextfo.follower_id = tuser.id
where tnote.date_created < NOW() and tnote.date_created > concat(date_sub(curdate(),interval 1 day),' ',curtime())
group by context_id, user_id
UNION 
SELECT tuser.id as user_id, tuser.first_name, tuser.email, tcontext.id as context_id, tcontext.context_name, count(tnote.id) 
FROM `tsaap-notes`.note as tnote 
INNER JOIN `tsaap-notes`.context as tcontext ON tnote.context_id = tcontext.id
INNER JOIN `tsaap-notes`.user as tuser ON tcontext.owner_id = tuser.id
where tnote.date_created < NOW() and tnote.date_created > concat(date_sub(curdate(),interval 1 day),' ',curtime())
group by context_id, user_id
order by user_id,context_name 
