//
//  ScheduleViewController.h
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/6/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScheduleViewController : UITableViewController

-(IBAction)refreshData:id;
-(IBAction)settingsButtonPressed:id;
-(IBAction)helpButtonPressed:id;
-(void)initButtons;

@end
