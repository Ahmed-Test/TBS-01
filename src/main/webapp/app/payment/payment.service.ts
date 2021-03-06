import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import * as moment from 'moment';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import {map} from 'rxjs/operators';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared/util/request-util';
import {IPayment} from 'app/shared/model/payment.model';
import {DataTableInput} from 'app/shared/model/datatable/datatable-input';
import {_tbs} from 'app/shared/util/tbs-utility';
import {Pageable} from 'app/shared/model/pageable';
import {IRefund} from "app/shared/model/refund.model";
import {IInvoiceSearchRequest} from "app/shared/model/invoice-serach-request";
import {IInvoice} from "app/shared/model/invoice.model";
import {PaymentSearchRequest} from "app/shared/model/payment-serach-request";

type EntityResponseType = HttpResponse<IPayment>;
type EntityArrayResponseType = HttpResponse<IPayment[]>;

@Injectable({ providedIn: 'root' })
export class PaymentService {
  public resourceUrl = SERVER_API_URL + 'api/payments';
  public resourceUrlCreditCard = '/billing/payments/credit-card';
  public resourceUrlRefund = '/billing/refunds';

  constructor(protected http: HttpClient) {}

  getList(datatableInput: DataTableInput): Observable<Pageable<IPayment>> {
    return this.http.get<Pageable<IPayment>>(`${this.resourceUrl}/datatable?${_tbs.serializeDataTableRequest(datatableInput)}`);
  }
  getPaymentBySearch(paymentSearchRequest : PaymentSearchRequest): Observable<Pageable<IPayment>> {
    return this.http.post<Pageable<IPayment>>(`${this.resourceUrl}/search`,paymentSearchRequest);
  }
  create(payment: IPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payment);
    return this.http
      .post<IPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  createCcPayment(payment: IPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payment);
    return this.http
      .post<IPayment>(this.resourceUrlCreditCard, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  createCcRefund(refund: IRefund): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(refund);
    return this.http
      .post<IPayment>(this.resourceUrlRefund, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(payment: IPayment): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(payment);
    return this.http
      .put<IPayment>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPayment[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(payment: IPayment): IPayment {
    const copy: IPayment = Object.assign({}, payment, {
      expirationDate: payment.expirationDate != null && payment.expirationDate.isValid() ? payment.expirationDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expirationDate = res.body.expirationDate != null ? moment(res.body.expirationDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((payment: IPayment) => {
        payment.expirationDate = payment.expirationDate != null ? moment(payment.expirationDate) : null;
      });
    }
    return res;
  }
}
