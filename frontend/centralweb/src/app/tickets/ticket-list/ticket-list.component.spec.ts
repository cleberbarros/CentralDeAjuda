import { TestBed } from '@angular/core/testing';
import { TicketListComponent } from './ticket-list.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

describe('TicketListComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        TicketListComponent,
        HttpClientTestingModule,
        RouterTestingModule
      ],
    }).compileComponents();
  });

  it('should create the ticket list component', () => {
    const fixture = TestBed.createComponent(TicketListComponent);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});
