# Address Api Spec

## Create Address

Endpoint : POST /api/contacts/${idContacts}/addresses

Request Header :
- X-API-Token : Token(Mandatory)

Request Body :

'{
    "street": "jalan",
    "city": "kota",
    "provinces":"provinsi",
    "country":"negara",
    "postalCode":"1234"
}
'

Response Body (Success) :
'{
"data" : "'{
"id":"random string"
"street": "jalan",
"city": "kota",
"provinces":"provinsi",
"country":"negara",
"postalCode":"1234"
}"

}'
Response Body (Failed) :
- '{
  "errors":"contact not found"
  } '

## Update Address

Endpoint : PUT /api/contacts/${idContacts}/addresses

Request Header :
- X-API-Token : Token(Mandatory)

Request Body :

'{
"street": "jalan",
"city": "kota",
"provinces":"provinsi",
"country":"negara",
"postalCode":"1234"
}
'

Response Body (Success) :
'{
"data" : "'{
"id":"random string"
"street": "jalan",
"city": "kota",
"provinces":"provinsi",
"country":"negara",
"postalCode":"1234"
}"

Response Body (Failed) :
- '{
  "errors":"contact not found"
  } '

## Get Address
Endpoint : GET /api/contacts/${idContacts}/addresses/{idAddress}

Request Header :
- X-API-Token : Token(Mandatory)

Response Body (Success) :
'{
"data" : "'{
"id":"random string"
"street": "jalan",
"city": "kota",
"provinces":"provinsi",
"country":"negara",
"postalCode":"1234"
}"

Response Body (Failed) :
- '{
  "errors":"address not found"
  } '

## Remove Address
Endpoint : Delete /api/contacts/${idContacts}/addresses/{idAddress}

Request Header :
- X-API-Token : Token(Mandatory)

Response Body (Success) :
- '{
  "data":"ok"
  } '

Response Body (Failed) :
- '{
  "errors":"address not found"
  } '

## List Address
Endpoint : GET /api/contacts/${idContacts}/addresses

Request Header :
- X-API-Token : Token(Mandatory)

Response Body (Success) :
'{
"data" : [
"'{
"id":"random string"
"street": "jalan",
"city": "kota",
"provinces":"provinsi",
"country":"negara",
"postalCode":"1234"
}"
]}

Response Body (Failed) :
- '{
  "errors":"contact not found"
  } '
