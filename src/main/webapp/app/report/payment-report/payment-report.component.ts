import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {Subscription} from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import {finalize} from 'rxjs/operators';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import {IItem} from 'app/shared/model/item.model';
import {AccountService} from 'app/core/auth/account.service';
import {DatatableComponent} from '@swimlane/ngx-datatable';
import {Datatable} from '../../shared/model/datatable/datatable';
import {ActivatedRoute, Router} from '@angular/router';
import {DatatableColumn} from 'app/shared/model/datatable/datatable-column';
import {PageQueryParams} from 'app/shared/model/page-query-params';
import {TranslateService} from '@ngx-translate/core';
import {ReportService} from 'app/report/report.service';
import {PaymentStatus, ReportStatus} from 'app/shared/constants';
import {NgbCalendar, NgbDate, NgbDateParserFormatter} from '@ng-bootstrap/ng-bootstrap';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap/datepicker/ngb-date-struct';
import {IClient} from 'app/shared/model/client.model';
import {ClientService} from 'app/client/client.service';
import {IPaymentSearchRequest} from 'app/shared/model/payment-serach-request';
import * as moment from 'moment';
import {IReport} from 'app/shared/model/report.model';
import {ZonedDateTime} from "js-joda";

@Component({
  selector: 'app-report',
  templateUrl: './payment-report.component.html'
})
export class PaymentReportComponent implements OnInit {

  eventSubscriber: Subscription;

  // new datatable
  busy = false;
  datatable = new Datatable<IItem>();
  // Datatable Reference
  @ViewChild('table', {static: true}) table: DatatableComponent;

  ReportStatus: ReportStatus;

  // Datatable Templates Reference
  @ViewChild('headerTemplate', {static: false}) headerTemplate;
  @ViewChild('rowTemplate', {static: false}) rowTemplate;
  @ViewChild('actionsRowTemplate', {static: true}) actionsRowTemplate;
  @ViewChild('editRowTemplate', {static: true}) editRowTemplate;
  @ViewChild('reportStatusRowTemplate', {static: true}) reportStatusRowTemplate;
  @ViewChild('dateTemplate', {static: true}) dateTemplate;
  constructor(
    protected reportService: ReportService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService,
    private activatedRoute: ActivatedRoute,
    private translateService: TranslateService,
    private router: Router,
    public formatter: NgbDateParserFormatter,
    private calendar: NgbCalendar,
    protected clientService: ClientService
  ) {
    const current = new Date();
    this.maxDate = {
      year: current.getFullYear(),
      month: current.getMonth() + 1,
      day: current.getDate()
    };
    const previous = new Date();
    previous.setMonth(previous.getMonth() - 1);
    this.startDate = {
      year: previous.getFullYear(),
      month: previous.getMonth() + 1,
      day: previous.getDate()
    };
  }

  hoveredDate: NgbDate;
  fromDate: NgbDate;
  toDate: NgbDate;
  maxDate: NgbDateStruct;
  startDate: NgbDateStruct;
  selectedClient: IClient;
  clients: IClient[];


  ngOnInit() {

    this.clientService.getClientByRole()
      .subscribe(
        res => {
          this.clients = res.body ;
        }, res => {
          console.log('An error has occurred when get clientByRole');
        }
      );

    this.initDatatable();

    this.activatedRoute.queryParams
      .subscribe((pageQueryParams: PageQueryParams) => {
        this.datatable.fillPageQueryParams(pageQueryParams);

        this.loadData();
      });
  }

  initDatatable() {
    this.datatable.setTable(this.table);
    this.datatable.setColumns([
      new DatatableColumn({
        name: this.translateService.instant('global.datatable.id'),
        prop: 'id',
        headerTemplate: this.headerTemplate,
        cellTemplate: this.rowTemplate,
      }),
      new DatatableColumn({
        name: this.translateService.instant('report.requestDate'),
        prop: 'requestDate',
        headerTemplate: this.headerTemplate,
        cellTemplate: this.dateTemplate
      }),
      new DatatableColumn({
        name: this.translateService.instant('report.generatedDate'),
        prop: 'generatedDate',
        headerTemplate: this.headerTemplate,
        cellTemplate: this.dateTemplate
      }),
      new DatatableColumn({
        name: this.translateService.instant('report.expireDate'),
        prop: 'expireDate',
        headerTemplate: this.headerTemplate,
        cellTemplate: this.dateTemplate
      }),
      new DatatableColumn({
        name: this.translateService.instant('report.status'),
        prop: 'status',
        headerTemplate: this.headerTemplate,
        cellTemplate: this.reportStatusRowTemplate
      }),
      new DatatableColumn({
        name: this.translateService.instant('report.download'),
        sortable: false,
        searchable: false,
        headerTemplate: this.headerTemplate,
        cellTemplate: this.actionsRowTemplate
      })
    ]);
  }

  loadData() {
    this.busy = true;
    this.reportService.getPaymentReportList(this.datatable.getDataTableInput())
      .pipe(
        finalize(() => this.busy = false)
      )
      .subscribe(
        (res) => {
          this.datatable.update(res);
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  filter(reset: boolean) {
    if (reset) {
      this.datatable.resetPageNumber();
    }

    const pageQueryParams = new PageQueryParams();

    pageQueryParams.fillDatatable(this.datatable);

    this.router.navigate(['/report'], {queryParams: pageQueryParams});
  }

  search() {
    this.filter(true);
  }

  clearSearch() {
    this.datatable.search = '';

    this.search();
  }

  onPageChanged(data) {
    this.datatable.changePageNumber(data.offset);

    this.filter(false);
  }

  onPageSizeChanged() {
    this.filter(true);
  }

  onSortChanged(data) {
    this.datatable.setSort(data.sorts[0]);

    this.filter(true);
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  // Calendar
  onDateSelection(date: NgbDate, datepicker) {
    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
    } else if (this.fromDate && !this.toDate && date.after(this.fromDate)) {
      this.toDate = date;
      datepicker.close();
    } else {
      this.toDate = null;
      this.fromDate = date;
    }
  }

  isHovered(date: NgbDate) {
    return this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate);
  }

  isInside(date: NgbDate) {
    return date.after(this.fromDate) && date.before(this.toDate);
  }

  isRange(date: NgbDate) {
    return date.equals(this.fromDate) || date.equals(this.toDate) || this.isInside(date) || this.isHovered(date);
  }

  validateInput(currentValue: NgbDate, input: string): NgbDate {
    const parsed = this.formatter.parse(input);
    return parsed && this.calendar.isValid(NgbDate.from(parsed)) ? NgbDate.from(parsed) : currentValue;
  }

  trackClientById(index: number, item: IClient) {
    return item.id;
  }

  clearReportFilters() {
    this.selectedClient = null;
    this.fromDate = null;
    this.toDate = null;
  }

  generateReport() {
    let toDate = null;
    let fromDate = null;
    let clientId = null;

    if (this.fromDate != null) {
      fromDate = this.formatDate(this.fromDate);
    }
    if (this.toDate != null) {
      toDate = this.formatDate(this.toDate).add(1, 'days');
    }
    if (this.selectedClient != null) {
      clientId = this.selectedClient.id;
    }
    const reportRequest: IReport = {
      startDate : fromDate,
      endDate : toDate,
      clientId : clientId,
      offset: ZonedDateTime.now().offset()._id
    };


    this.activatedRoute.queryParams
      .subscribe((pageQueryParams: PageQueryParams) => {
        this.datatable.fillPageQueryParams(pageQueryParams);
        this.reportService.requestPaymentReport(reportRequest)
          .subscribe(
            (res) => {
              this.initDatatable();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
          );
      });
  }

  formatDate(date: NgbDate) {
    const ngbObj = JSON.parse(JSON.stringify(date));
    if (ngbObj) {
      return moment(ngbObj.year + '-' + ngbObj.month + '-' + ngbObj.day, 'YYYY-MM-DD');
    }
  }

}
