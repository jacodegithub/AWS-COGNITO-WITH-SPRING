// const form = document.getElementById("kycForm");

// form.addEventListener("click", function(event) {
//     event.preventDefault(); // Prevent default form submission
    
//     console.log('this is form', form);
//   // Access form data
//   const firstName = document.querySelector("#name").value;
//   const phone = document.querySelector("#phone").value;
//   const pancard = document.querySelector("#pancard").value;
//   const aadhar = document.querySelector("#aadhar").value;

//   // Access and validate image files (basic example)
//   const # = document.querySelector("#aadharImage").files[0];
//   const pa#ncardImage = document.querySelector("#pancardImage").files[0]#;

// //   if (!aadharImage || !pancardImage) {
// //     alert("Please select both Aadhar and PAN card images.");
// //     return;
// //   }

//    const kycUser = {
//     firstName: firstName,
//     phone: phone,
//     panNumber: pancard,
//     aadharNumber: aadhar
//    }

// //   const formData = new FormData();
// //   formData.append("firstName", firstName);
// //   formData.append("phone", phone);
// //   formData.append("pancard", pancard);
// //   formData.append("aadhar", aadhar);
// //   formData.append("aadharImage", aadharImage);
// //   formData.append("pancardImage", pancardImage);

//   console.log("This is the form data ",kycUser);

//   axios.post("http://localhost:9090/kycuser", kycUser).then(response => {
//     console.log('KYC form submitted successfully:', response.kycUser);
//   })
//   .catch(error => {
//     console.error('Error submitting KYC form:', error);
//   })



// });


const form = document.getElementById("kycForm");

let aadharImage;
let pancardImage;

form.addEventListener("submit", function(event) {
    event.preventDefault(); 
    
    // Access form data
    const firstName = document.querySelector("#name").value;
    const kyc_email = document.querySelector("#email").value;
    const phone = document.querySelector("#phone").value;
    const pancard = document.querySelector("#pancard").value;
    const aadhar = document.querySelector("#aadhar").value;

    // Access image files
    // const aadharImage = document.querySelector("#aadharImage").files[0];
    // const pancardImage = document.querySelector("#pancardImage").files[0];

    const aadharImageInput = document.querySelector('#aadharImage');
    const aadharImagePreview = document.querySelector('#aadharPreview');
    const pancardImageInput = document.querySelector('#pancardImage');
    const pancardImagePreview = document.querySelector('#pancardPreview');

    const kycUser = {
        firstName: firstName,
        email: kyc_email,
        phone: phone,
        panNumber: pancard,
        aadharNumber: aadhar 
    };
// console.log(aadharImagePreview);

    aadharImageInput.addEventListener('change', function(event) {
      aadharImage = event.target.files[0];
      // console.log('file ->', file);
      if (aadharImage) {
          const reader = new FileReader();
          reader.onload = function(e) {
              aadharImagePreview.src = e.target.result;
              aadharImagePreview.style.display = 'block';
          };
          reader.readAsDataURL(aadharImage);
      } else {
          aadharImagePreview.src = '#';
          aadharImagePreview.style.display = 'none';
      }
  });


  
  pancardImageInput.addEventListener('change', function(event) {
    pancardImage = event.target.files[0];
      if (pancardImage) {
          const reader = new FileReader();
          reader.onload = function(e) {
              pancardImagePreview.src = e.target.result;
              pancardImagePreview.style.display = 'block';
          };
          reader.readAsDataURL(pancardImage);
      } else {
          pancardImagePreview.src = '#';
          pancardImagePreview.style.display = 'none';
      }
  });

  console.log('aadhar image, pan card', aadharImage, pancardImage);

    let email;
    let userId;

  axios.post("http://localhost:9090/user/kycData", kycUser)
  .then(response => {
      email = response.data.email;
      console.log('this is the id', response.data.email);
      console.log('KYC form submitted successfully:', response.data);


          if(!email) {
            console.log("Improper emailId");
            return
          }

          axios.get(`http://localhost:9090/user/${email}`)
          .then(response => {
              userId = response.data.id;
              console.log('this is the email', response.data);
              console.log('KYC form submitted successfully:', response.data);


              if(!userId) {
                console.log("Improper userId");
                return
              }

              let combinedFormData = new FormData();
              combinedFormData.append("aadharImage", aadharImage);
              combinedFormData.append("pancardImage", pancardImage);

              console.log(combinedFormData);
              axios.post(`http://localhost:9090/user/${userId}/kyc`, combinedFormData, {
                headers: {
                  "Content-Type": "multipart/form-data",
                },
              })
              .then(response => {
                  console.log('Images submitted successfull!!', response.data);
              })
              .catch(error => {
                  console.log('Error submitting images', error);
              })

          })
          .catch(error => {
              console.error('Error submitting KYC form:', error);
          });
  })
  .catch(error => {
      console.error('Error submitting KYC form:', error);
  });

      // if(!email) return;
  
      
    
  // if(!userId) return;



});

