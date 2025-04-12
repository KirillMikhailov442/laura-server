export interface IDefaultMail {
  to: string;
  subject: string;
  message: string;
}

export interface IConfirmMail {
  to: string;
  username: string;
  pincode: string;
}

export interface IBanMail {
  to: string;
  username: string;
}
