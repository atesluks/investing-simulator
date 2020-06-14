import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AllStocksListComponent } from './all-stocks-list.component';

describe('AllStocksListComponent', () => {
  let component: AllStocksListComponent;
  let fixture: ComponentFixture<AllStocksListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllStocksListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllStocksListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
