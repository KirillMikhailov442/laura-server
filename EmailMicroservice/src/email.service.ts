import nodemailer from 'nodemailer';
import ejs from 'ejs';
import { IBanMail, IConfirmMail, IDefaultMail } from './types';
require('dotenv').config();
import path from 'path';

class EmailService {
  private host: string;
  private port: number;
  private sender: string;
  private password: string;
  private mailer: nodemailer.Transporter | undefined;

  constructor(host: string, port: number, sender: string, password: string) {
    this.host = host;
    this.port = port;
    this.sender = sender;
    this.password = password;
    this.mailer = this.configTransport;
  }

  private get configTransport() {
    return nodemailer.createTransport({
      host: this.host,
      port: this.port,
      secure: true,
      auth: {
        user: this.sender,
        pass: this.password,
      },
    });
  }

  private async renderTemplate<T>(
    path: string,
    data: T | any
  ): Promise<string> {
    return new Promise((resolve, reject) => {
      ejs.renderFile(path, data, (err, str) => {
        if (err) {
          reject(err);
        } else {
          resolve(str);
        }
      });
    });
  }

  public async sendConfirmMail(option: IConfirmMail) {
    const html = await this.renderTemplate<IConfirmMail>(
      path.resolve(__dirname, '..', 'templates', 'mail-confirm.ejs'),
      option
    );
    if (!html) throw new Error('Template not found');

    try {
      this.mailer?.sendMail({
        to: option.to,
        from: this.sender,
        subject: 'Confirm email for Laura',
        html,
      });
    } catch {
      throw new Error('failed to send email');
    }
  }

  public async sendBanMail(option: IBanMail) {
    const html = await this.renderTemplate<IBanMail>(
      path.resolve(__dirname, '..', 'templates', 'mail-ban.ejs'),
      option
    );
    if (!html) throw new Error('Template not found');

    try {
      this.mailer?.sendMail({
        to: option.to,
        from: this.sender,
        subject: 'Ban account in Laura',
        html,
      });
    } catch {
      throw new Error('failed to send email');
    }
  }

  public async sendDefaultMail(option: IDefaultMail) {
    const html = await this.renderTemplate<IDefaultMail>(
      path.resolve(__dirname, '..', 'templates', 'defaultMail.ejs'),
      option
    );
    if (!html) throw new Error('Template not found');

    try {
      this.mailer?.sendMail({
        to: option.to,
        from: this.sender,
        subject: option.subject,
        html,
      });
    } catch {
      throw new Error('failed to send email');
    }
  }
}

export default new EmailService(
  String(process.env.EMAIL_HOST),
  Number(process.env.EMAIL_PORT),
  String(process.env.EMAIL_USER),
  String(process.env.EMAIL_PASSWORD)
);
