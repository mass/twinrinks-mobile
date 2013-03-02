//
//  UpcomingViewController.h
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/6/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UpcomingViewController : UITableViewController

@property (nonatomic,strong) __block NSArray *gameArray;
@property (nonatomic,strong) __block NSArray *teamArray;
@property (nonatomic,strong) __block NSArray *yourTeamArray;


-(IBAction)doNothing:id;
-(IBAction)settingsButtonPressed:id;
-(IBAction)helpButtonPressed:id;
-(void)initButtons;

@end