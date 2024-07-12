# Contact API Spec

## Create Contact

Endpoint : POST /api/contacts

Request Header :
- X-API-Token : Token(Mandatory)

Request Body :
''' json
{
"firstName":"Tia",
"lastName":"Sarwoedhi",
"email":"tia@gmail.com",
"phone":"08982131232"
}'''

Response Body (Success) :
'''{
"data" : {
"id":"random string",
"firstName":"Tia",
"lastName":"Sarwoedhi",
"email":"tia@gmail.com",
"phone":"08982131232"
}
}'''

Response Body (Failed) : 
- '{
  "errors":"Email format or phone format invalid" } '

## Update Contact
Endpoint : PUT /api/contacts/{idContact}

Request Header :
- X-API-Token : Token(Mandatory)

Request Body :
''' json
{
"firstName":"Tia",
"lastName":"Sarwoedhi",
"email":"tia@gmail.com",
"phone":"08982131232"
}'''

Response Body (Success) :
'''{
"data" : {
"id":"random string",
"firstName":"Tia",
"lastName":"Sarwoedhi",
"email":"tia@gmail.com",
"phone":"08982131232"
}
}'''

Response Body (Failed) :
- '{
  "errors":"Email format or phone format invalid" } '

## Get Contact
Endpoint : GET /api/contacts/{idContact}


Request Header :
- X-API-Token : Token(Mandatory)


Response Body (Success) :
'''{
"data" : {
"id":"random string",
"firstName":"Tia",
"lastName":"Sarwoedhi",
"email":"tia@gmail.com",
"phone":"08982131232"
}
}'''

Response Body (Failed) :
- '{
  "errors":"Contact not found"
} '

## Search Contact

Endpoint : GET  /api/contacts

Request Param / Query Param

- name : String, contact firstname or lastname, using like query, optional
- phone: String, contact phone, using phone, optional
- email: String, contact email, using like query, optional
- page : Integer, start from 0, default 0
- size: Integer, default 10

Request Header :
- X-API-Token : Token(Mandatory)

Response Body (Success) :
'''json {
"data" : [
{
"id":"random string",
"firstName":"Tia",
"lastName":"Sarwoedhi",
"email":"tia@gmail.com",
"phone":"08982131232"
}
],
"paging": {
    "current":0,
    "totalPage": 10,
    "size": 10
}
}'''

Response Body (Failed) :
- '{
  "errors":"Unauthorized"
  } '


## Remove Contact

Endpoint : DELETE /api/contacts/{idContact}

Request Header :
- X-API-Token : Token(Mandatory)

Response Body (Success) :
- '{
  "data":"Ok"
  } '


Response Body (Failed) : 
- '{
  "errors":"Contact not found"
  } '