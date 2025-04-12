import express, { Request, Response } from 'express';
import emailService from './email.service';
require('dotenv').config();

const app = express();
const port = process.env.PORT;

app.get('/', (req: Request, res: Response) => {
  emailService.sendDefaultMail({
    to: 'akcjdjs123456789@gmail.com',
    subject: 'Hello me',
    message: 'Hello from laura',
  });

  emailService.sendBanMail({
    to: 'akcjdjs123456789@gmail.com',
    username: 'mojave',
  });

  emailService.sendConfirmMail({
    to: 'akcjdjs123456789@gmail.com',
    username: 'Mojave',
    pincode: '123456',
  });
  res.send('Hello, Express with TypeScript!');
});

app.listen(port, () => {
  console.log(`Server is running at http://localhost:${port}`);
});
