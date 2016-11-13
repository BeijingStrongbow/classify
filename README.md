#Classify

An all in one platform to incentivize students to behave better and to help keep their assignments in check.

##Inspiration

In modern-day elementary school classrooms, the class can often feel separated from the parents of the students. Parents may ask their child what they did in school that day. However, students won't necessarily be able to give all the detail that the parents want to know, such as assigned homework, due dates, and behavior.

##What it does

Classify fixes this by closing the distance between parents and the classroom. When a teacher sees a student doing something good or bad, he/she can ask Alexa to give or remove a student's points. The student's total points are then sent to www.echoclassify.tech for parents to view. Parents can also view all of the assignments (with due dates) that the teacher added using an Amazon Echo. This will allow them to help their child complete their assignments on time. In addition to all of this, if a parent has any concerns about why their child has the point total they have, or wants more information about an assignment, he/she can send an email to the teacher directly off of our website.

##How we built it

Our team used an Amazon Echo in conjunction with Amazon Web Services Lambda to send points and assignments to Parse Server. Then, www.echoclassify.tech retrieved data from Parse to show it on the website. Using emailjs, we were able to allow parents to send emails to the teacher directly off the website.

###Built with
java, javascript, html5, css3, php, twilio, Parse, Smazon Web Services, email.js