// AWS.config.region = 'us-east-1';
// AWS.config.credentials = new AWS.CognitoIdentityCredentials({
//     IdentityPoolId: 'us-east-1_w5BD2MI1A'
// });


// var authenticationData = {
//     Username : 'username',
//     Password : 'password',
// };

// var authenticationDetails = new AmazonCognitoIdentity.AuthenticationDetails(authenticationData);

// var poolData = {
//     UserPoolId : 'YOUR_USER_POOL_ID',
//     ClientId : 'YOUR_APP_CLIENT_ID'
// };

// var userPool = new AmazonCognitoIdentity.CognitoUserPool(poolData);
// var userData = {
//     Username : 'username',
//     Pool : userPool
// };

// var cognitoUser = new AmazonCognitoIdentity.CognitoUser(userData);
// cognitoUser.authenticateUser(authenticationDetails, {
//     onSuccess: function (result) {
//         console.log('Authentication successful');
//         var accessToken = result.getAccessToken().getJwtToken();
//         // Now, you have the accessToken, which you can use for accessing secured APIs
//     },
//     onFailure: function(err) {
//         console.error('Authentication failed', err);
//     },
//     newPasswordRequired: function(userAttributes, requiredAttributes) {
//         // User needs to set a new password
//         console.log('New password required', userAttributes, requiredAttributes);
//     }
// });
