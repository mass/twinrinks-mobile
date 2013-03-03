//
//  ScheduleGamesViewController.h
//  TwinRinksAdultHockey
//
//  Created by Andrew on 1/8/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScheduleGamesViewController : UITableViewController

@property (nonatomic,strong) __block NSArray *gameArray;
@property (nonatomic,strong) __block NSString *dataToDisplay;

@end
