# User API spec

## Register User

- Endpoint: POST /api/users 

- Request Body

'json
{
    "username":"edhitia",
    "password":"rahasia",
    "name":"Tia Sarwoedhi"
}
'

Response Body (Success):
{
"data":"Ok"
}

Response Body (Failed):
{
"errors":"Username must not blank"
}

## Login User
- Endpoint: POST /api/auth/login
 
- Request Body

{
  "username":"edhitia",
  "password":"rahasia",
  }


- Response Body (Success):
  {
  "token":"Token",
  "expiredAt": 123142142141
  }

- Response Body (Failed)
  {
  "errors":"Username must not blank"
  }
## Get User

- Endpoint: GET /api/users/current
- X-API-Token : Token(Mandatory)

Response Body (Success):
{
"data":"Ok",
"username":"edhitia",
"name":"Tia Sarwoedhi"
}

Response Body (Failed)
- '{
  "errors":"unauthorized !" } '

## Update User
Endpoint: Patch /api/users/current
    //patch : untuk mengupdate salah satu field tanpa menimpa data seluruhnya.
   //put : untuk mengupdate seluruh data

Request Header :
- X-API-Token : Token(Mandatory)

Response Body (Success):
  {
  "name":"Tia",//put if only want to update name
  "password": 123142142141 // //put if only want to update password
  }
- 
Response Body (Success):
{
"username":"edhitia",
"name":"Tia Sarwoedhi"
}


## Logout User
- Endpoint: Patch /api/auth/logout

Request Header :
- 'X-API-Token : Token(Mandatory)'

Response Body (Success):
{
"data":"Ok",
}
