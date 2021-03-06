import { Moment } from 'moment';
import { IInvoiceItem } from 'app/shared/model/invoice-item.model';
import { IPayment } from 'app/shared/model/payment.model';
import { InvoiceStatus } from 'app/shared/model/enumerations/invoice-status.model';
import {PaymentStatus} from 'app/shared/constants';

export interface IInvoice {
  id?: number;
  status?: InvoiceStatus;
  number?: number;
  note?: string;
  dueDate?: Moment;
  subtotal?: number;
  amount?: number;
  discountId?: number;
  customerId?: string;
  invoiceItems?: IInvoiceItem[];
  payments?: IPayment[];
  paymentStatus?: PaymentStatus;
  clientId?: number;
  accountId?: number;

}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public status?: InvoiceStatus,
    public number?: number,
    public note?: string,
    public dueDate?: Moment,
    public subtotal?: number,
    public amount?: number,
    public discountId?: number,
    public customerId?: string,
    public invoiceItems?: IInvoiceItem[],
    public payments?: IPayment[],
    public clientId?: number,
    public accountId?: number,
    paymentStatus?: PaymentStatus
  ) {}
}
