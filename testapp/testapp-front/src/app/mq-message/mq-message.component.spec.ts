import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MqMessage } from './mq-message';

describe('MqMessage', () => {
  let component: MqMessage;
  let fixture: ComponentFixture<MqMessage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MqMessage],
    }).compileComponents();

    fixture = TestBed.createComponent(MqMessage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
